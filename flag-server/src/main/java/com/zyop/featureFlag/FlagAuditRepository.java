package com.zyop.featureFlag;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FlagAuditRepository extends JpaRepository<FlagAudit, Long> {
    List<FlagAudit> findByFlagIdOrderByChangedAtDesc(Long flagId);
    List<FlagAudit> findAllByOrderByChangedAtDesc();
}
