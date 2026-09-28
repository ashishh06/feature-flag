package com.zyop.featureFlag.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateFlagRequest {

    @NotBlank(message = "Key is required")
    @Size(max = 100, message = "Key must be at most 100 characters")
    private String key;

    @NotBlank(message = "Name is required")
    @Size(max = 200, message = "Name must be at most 200 characters")
    private String name;

    private boolean enabled;
}
