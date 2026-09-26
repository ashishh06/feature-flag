package com.zyop.featureFlag.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateFlagRequest {
    private String key;
    private String name;
    private boolean enabled;
}
