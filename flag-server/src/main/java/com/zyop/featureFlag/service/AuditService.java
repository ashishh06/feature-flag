package com.zyop.featureFlag.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zyop.featureFlag.Flag;
import com.zyop.featureFlag.FlagAudit;
import com.zyop.featureFlag.FlagAuditRepository;
import com.zyop.featureFlag.FlagRule;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuditService {

    private final FlagAuditRepository flagAuditRepository;
    private final ObjectMapper objectMapper;

    public AuditService(FlagAuditRepository flagAuditRepository, ObjectMapper objectMapper) {
        this.flagAuditRepository = flagAuditRepository;
        this.objectMapper = objectMapper;
    }

    public void logCreate(Flag flag, String changedBy) {
        try {
            String newValue = objectMapper.writeValueAsString(flag);
            FlagAudit audit = new FlagAudit(flag.getId(), "CREATE", null, newValue, changedBy);
            flagAuditRepository.save(audit);
        } catch (Exception e) {
            // Don't fail the operation if audit logging fails
            System.err.println("Failed to log audit: " + e.getMessage());
        }
    }

    public void logToggle(Flag flag, boolean oldValue, String changedBy) {
        try {
            FlagAudit audit = new FlagAudit(
                    flag.getId(),
                    "TOGGLE",
                    String.valueOf(oldValue),
                    String.valueOf(flag.isEnabled()),
                    changedBy
            );
            flagAuditRepository.save(audit);
        } catch (Exception e) {
            System.err.println("Failed to log audit: " + e.getMessage());
        }
    }

    public void logUpdate(Flag flag, String oldValue, String changedBy) {
        try {
            String newValue = objectMapper.writeValueAsString(flag);
            FlagAudit audit = new FlagAudit(flag.getId(), "UPDATE", oldValue, newValue, changedBy);
            flagAuditRepository.save(audit);
        } catch (Exception e) {
            System.err.println("Failed to log audit: " + e.getMessage());
        }
    }

    public void logDelete(Flag flag, String changedBy) {
        try {
            String oldValue = objectMapper.writeValueAsString(flag);
            FlagAudit audit = new FlagAudit(flag.getId(), "DELETE", oldValue, null, changedBy);
            flagAuditRepository.save(audit);
        } catch (Exception e) {
            System.err.println("Failed to log audit: " + e.getMessage());
        }
    }

    public void logAddRule(FlagRule rule, String changedBy) {
        try {
            String newValue = objectMapper.writeValueAsString(rule);
            FlagAudit audit = new FlagAudit(rule.getFlagId(), "ADD_RULE", null, newValue, changedBy);
            flagAuditRepository.save(audit);
        } catch (Exception e) {
            System.err.println("Failed to log audit: " + e.getMessage());
        }
    }

    public List<FlagAudit> getAuditHistory(Long flagId) {
        return flagAuditRepository.findByFlagIdOrderByChangedAtDesc(flagId);
    }

    public List<FlagAudit> getAllAuditHistory() {
        return flagAuditRepository.findAllByOrderByChangedAtDesc();
    }
}
