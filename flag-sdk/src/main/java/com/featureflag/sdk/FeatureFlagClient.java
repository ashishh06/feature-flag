package com.featureflag.sdk;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FeatureFlagClient implements AutoCloseable {

    private final Map<String, Boolean> flagCache = new ConcurrentHashMap<>();
    private final FlagPoller poller;

    public FeatureFlagClient(String serverBaseUrl) {
        this.poller = new FlagPoller(serverBaseUrl, flagCache);
    }

    public void start() {
        poller.start();
    }

    @Override
    public void close() {
        poller.stop();
    }

    public boolean isEnabled(String flagKey) {
        return flagCache.getOrDefault(flagKey, false);
    }
}
