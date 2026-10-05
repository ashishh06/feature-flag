package com.zyop.featureFlag.dto;

public class EvaluationResponse {
    private String flagKey;
    private boolean enabled;
    private String reason;

    public EvaluationResponse(String flagKey, boolean enabled, String reason) {
        this.flagKey = flagKey;
        this.enabled = enabled;
        this.reason = reason;
    }

    public String getFlagKey() { return flagKey; }
    public boolean isEnabled() { return enabled; }
    public String getReason() { return reason; }
}
