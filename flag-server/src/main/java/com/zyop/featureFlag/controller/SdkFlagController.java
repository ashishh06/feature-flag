package com.zyop.featureFlag.controller;

import com.zyop.featureFlag.FlagRepository;
import com.zyop.featureFlag.dto.SdkFlagResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/sdk/flags")
public class SdkFlagController {

    private final FlagRepository flagRepository;

    public SdkFlagController(FlagRepository flagRepository) {
        this.flagRepository = flagRepository;
    }

    @GetMapping
    public List<SdkFlagResponse> getFlagsForSdk() {
        return flagRepository.findByArchivedFalse()
                .stream()
                .map(flag -> new SdkFlagResponse(flag.getKey(), flag.isEnabled()))
                .toList();
    }
}
