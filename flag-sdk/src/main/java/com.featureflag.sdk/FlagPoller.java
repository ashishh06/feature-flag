package com.featureflag.sdk;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class FlagPoller {

    private final String flagsUrl;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final Map<String, Boolean> flagCache;
    private final ScheduledExecutorService scheduler;

    public FlagPoller(String serverBaseUrl, Map<String, Boolean> flagCache) {
        this.flagsUrl = serverBaseUrl + "/api/sdk/flags";
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
        this.flagCache = flagCache;
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
    }

    // refresh once immediately, then every 30s after that
    public void start() {
        refresh();
        scheduler.scheduleAtFixedRate(this::refresh, 30, 30, TimeUnit.SECONDS);
    }

    public void stop() {
        scheduler.shutdown();
    }

    private void refresh() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(flagsUrl))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            List<SdkFlagDto> flags = objectMapper.readValue(
                    response.body(),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, SdkFlagDto.class)
            );

            for (SdkFlagDto flag : flags) {
                flagCache.put(flag.getKey(), flag.isEnabled());
            }

            System.out.println("[flag-sdk] refreshed " + flags.size() + " flags");
        } catch (IOException | InterruptedException e) {
            // a failed refresh should never crash the client app — just keep serving the last known values
            System.out.println("[flag-sdk] refresh failed, using last known flags: " + e.getMessage());
        }
    }
}
