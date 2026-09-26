package com.zyop.featureFlag.controller;

import com.zyop.featureFlag.Flag;
import com.zyop.featureFlag.FlagRepository;
import com.zyop.featureFlag.dto.CreateFlagRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/admin/flags")
public class FlagController {

    private final FlagRepository flagRepository;

    // Spring automatically injects the repository here — no "new" needed
    public FlagController(FlagRepository flagRepository) {
        this.flagRepository = flagRepository;
    }

    @GetMapping
    public List<Flag> getAllFlags() {
        return flagRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<Flag> createFlag(@RequestBody CreateFlagRequest request) {
        Flag flag = new Flag(request.getKey(), request.getName(), request.isEnabled());
        Flag saved = flagRepository.save(flag);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}/toggle")
    public ResponseEntity<Flag> toggleFlag(@PathVariable Long id) {
        Flag flag = flagRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Flag not found"));

        flag.setEnabled(!flag.isEnabled());
        Flag updated = flagRepository.save(flag);
        return ResponseEntity.ok(updated);
    }
}