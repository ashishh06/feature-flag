package com.zyop.featureFlag;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FlagRuleRepository extends JpaRepository<FlagRule, Long> {
    List<FlagRule> findByFlagIdOrderByPriorityAsc(Long flagId);
    void deleteByFlagId(Long flagId);
}
