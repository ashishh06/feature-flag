package com.zyop.featureFlag.controller;

import com.zyop.featureFlag.Flag;
import com.zyop.featureFlag.FlagRepository;
import com.zyop.featureFlag.dto.CreateFlagRequest;
import com.zyop.featureFlag.dto.UpdateFlagRequest;
import com.zyop.featureFlag.exception.DuplicateFlagKeyException;
import com.zyop.featureFlag.exception.FlagNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/flags")
public class FlagController {

    private final FlagRepository flagRepository;

    public FlagController(FlagRepository flagRepository) {
        this.flagRepository = flagRepository;
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
    public ResponseEntity<Flag> createFlag(@Valid @RequestBody CreateFlagRequest request) {
        if (flagRepository.existsByKey(request.getKey())) {
            throw new DuplicateFlagKeyException(request.getKey());
        }
        Flag flag = new Flag(request.getKey(), request.getName(), request.isEnabled());
        Flag saved = flagRepository.save(flag);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}/toggle")
    public ResponseEntity<Flag> toggleFlag(@PathVariable Long id) {
        Flag flag = flagRepository.findById(id)
                .orElseThrow(() -> new FlagNotFoundException(id));

        flag.setEnabled(!flag.isEnabled());
        Flag updated = flagRepository.save(flag);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Flag> updateFlag(@PathVariable Long id, @Valid @RequestBody UpdateFlagRequest request) {
        Flag flag = flagRepository.findById(id)
                .orElseThrow(() -> new FlagNotFoundException(id));

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
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFlag(@PathVariable Long id) {
        Flag flag = flagRepository.findById(id)
                .orElseThrow(() -> new FlagNotFoundException(id));

        flag.setArchived(true);
        flagRepository.save(flag);
        return ResponseEntity.noContent().build();
    }
}
