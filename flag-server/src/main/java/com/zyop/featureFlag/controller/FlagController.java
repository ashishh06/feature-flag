package com.zyop.featureFlag.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zyop.featureFlag.Flag;
import com.zyop.featureFlag.FlagRepository;
import com.zyop.featureFlag.dto.CreateFlagRequest;
import com.zyop.featureFlag.dto.UpdateFlagRequest;
import com.zyop.featureFlag.exception.DuplicateFlagKeyException;
import com.zyop.featureFlag.exception.FlagNotFoundException;
import com.zyop.featureFlag.service.AuditService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/flags")
public class FlagController {

    private final FlagRepository flagRepository;
    private final AuditService auditService;
    private final ObjectMapper objectMapper;

    public FlagController(FlagRepository flagRepository, AuditService auditService, ObjectMapper objectMapper) {
        this.flagRepository = flagRepository;
        this.auditService = auditService;
        this.objectMapper = objectMapper;
    }

    @GetMapping
    public List<Flag> getAllFlags() {
        return flagRepository.findByArchivedFalse();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Flag> getFlag(@PathVariable Long id) {
        Flag flag = flagRepository.findById(id)
                .orElseThrow(() -> new FlagNotFoundException(id));
        return ResponseEntity.ok(flag);
    }

    @PostMapping
    public ResponseEntity<Flag> createFlag(@Valid @RequestBody CreateFlagRequest request,
                                           @AuthenticationPrincipal UserDetails userDetails) {
        if (flagRepository.existsByKey(request.getKey())) {
            throw new DuplicateFlagKeyException(request.getKey());
        }
        Flag flag = new Flag(request.getKey(), request.getName(), request.isEnabled());
        Flag saved = flagRepository.save(flag);

        // Log the creation
        auditService.logCreate(saved, userDetails.getUsername());

        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}/toggle")
    public ResponseEntity<Flag> toggleFlag(@PathVariable Long id,
                                           @AuthenticationPrincipal UserDetails userDetails) {
        Flag flag = flagRepository.findById(id)
                .orElseThrow(() -> new FlagNotFoundException(id));

        boolean oldValue = flag.isEnabled();
        flag.setEnabled(!flag.isEnabled());
        Flag updated = flagRepository.save(flag);

        // Log the toggle
        auditService.logToggle(updated, oldValue, userDetails.getUsername());

        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Flag> updateFlag(@PathVariable Long id,
                                           @Valid @RequestBody UpdateFlagRequest request,
                                           @AuthenticationPrincipal UserDetails userDetails) {
        Flag flag = flagRepository.findById(id)
                .orElseThrow(() -> new FlagNotFoundException(id));

        String oldValue;
        try {
            oldValue = objectMapper.writeValueAsString(flag);
        } catch (Exception e) {
            oldValue = "{}";
        }

        if (request.getName() != null) {
            flag.setName(request.getName());
        }
        if (request.getDescription() != null) {
            flag.setDescription(request.getDescription());
        }
        if (request.getEnabled() != null) {
            flag.setEnabled(request.getEnabled());
        }

        Flag updated = flagRepository.save(flag);

        // Log the update
        auditService.logUpdate(updated, oldValue, userDetails.getUsername());

        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFlag(@PathVariable Long id,
                                           @AuthenticationPrincipal UserDetails userDetails) {
        Flag flag = flagRepository.findById(id)
                .orElseThrow(() -> new FlagNotFoundException(id));

        flag.setArchived(true);
        flagRepository.save(flag);

        // Log the deletion
        auditService.logDelete(flag, userDetails.getUsername());

        return ResponseEntity.noContent().build();
    }
}
