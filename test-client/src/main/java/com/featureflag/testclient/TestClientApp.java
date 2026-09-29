package com.featureflag.testclient;

import com.featureflag.sdk.FeatureFlagClient;

public class TestClientApp {

    public static void main(String[] args) throws InterruptedException {
        FeatureFlagClient client = new FeatureFlagClient("http://localhost:8080");
        client.start();

        // Register shutdown hook for graceful cleanup
        Runtime.getRuntime().addShutdownHook(new Thread(client::close));

        String flagKey = "dark-mode";

        while (true) {
            boolean enabled = client.isEnabled(flagKey);
            System.out.println("[test-client] '" + flagKey + "' is " + (enabled ? "ON" : "OFF"));
            Thread.sleep(5000);
        }
    }
}
