package com.zyop.featureFlag.controller;

import com.zyop.featureFlag.FlagAudit;
import com.zyop.featureFlag.service.AuditService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/audit")
public class AuditController {

    private final AuditService auditService;

    public AuditController(AuditService auditService) {
        this.auditService = auditService;
    }

    @GetMapping
    public ResponseEntity<List<FlagAudit>> getAllAuditHistory() {
        return ResponseEntity.ok(auditService.getAllAuditHistory());
    }

    @GetMapping("/{flagId}")
    public ResponseEntity<List<FlagAudit>> getAuditHistory(@PathVariable Long flagId) {
        return ResponseEntity.ok(auditService.getAuditHistory(flagId));
    }
}
