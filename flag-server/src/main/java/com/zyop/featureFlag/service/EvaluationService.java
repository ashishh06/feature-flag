package com.zyop.featureFlag.service;

import com.zyop.featureFlag.Flag;
import com.zyop.featureFlag.FlagRepository;
import com.zyop.featureFlag.FlagRule;
import com.zyop.featureFlag.FlagRuleRepository;
import com.zyop.featureFlag.dto.EvaluationRequest;
import com.zyop.featureFlag.dto.EvaluationResponse;
import com.zyop.featureFlag.exception.FlagNotFoundException;
import com.zyop.featureFlag.exception.FlagKeyNotFoundException;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Service
public class EvaluationService {

    private final FlagRepository flagRepository;
    private final FlagRuleRepository flagRuleRepository;

    public EvaluationService(FlagRepository flagRepository, FlagRuleRepository flagRuleRepository) {
        this.flagRepository = flagRepository;
        this.flagRuleRepository = flagRuleRepository;
    }

    public EvaluationResponse evaluate(EvaluationRequest request) {
        Flag flag = flagRepository.findByKey(request.getFlagKey())
                .orElseThrow(() -> new FlagKeyNotFoundException(request.getFlagKey()));

        if (flag.isArchived()) {
            return new EvaluationResponse(flag.getKey(), false, "Flag is archived");
        }

        if (!flag.isEnabled()) {
            return new EvaluationResponse(flag.getKey(), false, "Flag is disabled");
        }

        List<FlagRule> rules = flagRuleRepository.findByFlagIdOrderByPriorityAsc(flag.getId());

        if (rules.isEmpty()) {
            // No rules — flag is enabled for everyone
            return new EvaluationResponse(flag.getKey(), true, "No rules — enabled for all");
        }

        for (FlagRule rule : rules) {
            switch (rule.getType()) {
                case USER_ID:
                    if (evaluateUserIdRule(rule, request.getUserId())) {
                        return new EvaluationResponse(flag.getKey(), true, "User targeted: " + request.getUserId());
                    }
                    break;
                case GROUP:
                    if (evaluateGroupRule(rule, request.getGroup())) {
                        return new EvaluationResponse(flag.getKey(), true, "Group targeted: " + request.getGroup());
                    }
                    break;
                case PERCENTAGE:
                    if (evaluatePercentageRule(rule, request.getUserId(), flag.getKey())) {
                        return new EvaluationResponse(flag.getKey(), true, "Percentage rollout: " + rule.getValue() + "%");
                    }
                    break;
            }
        }

        return new EvaluationResponse(flag.getKey(), false, "No rules matched");
    }

    private boolean evaluateUserIdRule(FlagRule rule, String userId) {
        if (userId == null) return false;
        return rule.getValue().equals(userId);
    }

    private boolean evaluateGroupRule(FlagRule rule, String group) {
        if (group == null) return false;
        return rule.getValue().equals(group);
    }

    private boolean evaluatePercentageRule(FlagRule rule, String userId, String flagKey) {
        if (userId == null) return false;

        int percentage = Integer.parseInt(rule.getValue());
        int bucket = getBucket(userId + flagKey);
        return bucket < percentage;
    }

    /**
     * Consistent hashing: hash userId + flagKey to a bucket (0-99)
     * Same userId + flagKey always returns the same bucket
     */
    private int getBucket(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            // Use first 4 bytes of hash to get a number
            int hashValue = ((hash[0] & 0xFF) << 24) |
                           ((hash[1] & 0xFF) << 16) |
                           ((hash[2] & 0xFF) << 8)  |
                           (hash[3] & 0xFF);
            return Math.abs(hashValue) % 100;
        } catch (NoSuchAlgorithmException e) {
            // Fallback to simple hash
            return Math.abs(input.hashCode()) % 100;
        }
    }
}
