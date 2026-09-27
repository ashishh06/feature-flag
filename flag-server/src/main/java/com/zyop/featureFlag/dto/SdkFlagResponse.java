package com.zyop.featureFlag.dto;

public class SdkFlagResponse {
    private String key;
    private boolean enabled;

    public SdkFlagResponse(String key, boolean enabled) {
        this.key = key;
        this.enabled = enabled;
    }

    public String getKey() { return key; }
    public boolean isEnabled() { return enabled; }
}
