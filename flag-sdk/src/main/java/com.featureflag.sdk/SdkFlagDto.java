package com.featureflag.sdk;

public class SdkFlagDto {
    private String key;
    private boolean enabled;

    public String getKey() { return key; }
    public void setKey(String key) { this.key = key; }

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
}
