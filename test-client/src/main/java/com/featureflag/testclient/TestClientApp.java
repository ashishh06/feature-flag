package com.featureflag.testclient;

import com.featureflag.sdk.FeatureFlagClient;

public class TestClientApp {

    public static void main(String[] args) throws InterruptedException {
        FeatureFlagClient client = new FeatureFlagClient("http://localhost:8080");
        client.start();

        String flagKey = "dark-mode"; // use whichever key you already created via the admin API

        // loop forever, checking the flag every 5 seconds
        while (true) {
            boolean enabled = client.isEnabled(flagKey);
            System.out.println("[test-client] '" + flagKey + "' is " + (enabled ? "ON" : "OFF"));
            Thread.sleep(5000);
        }
    }
}
