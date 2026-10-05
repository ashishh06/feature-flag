package com.zyop.featureFlag.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EvaluationRequest {
    private String flagKey;
    private String userId;
    private String group;
}
