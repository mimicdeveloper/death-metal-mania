package com.deathmetalmania.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

@Service
public class SpotifyTokenService {

    @Value("${spotify.client.id}")
    private String clientId;

    @Value("${spotify.client.secret}")
    private String clientSecret;

    @Value("${spotify.token.url:https://accounts.spotify.com/api/token}")
    private String tokenUrl;

    private String accessToken;
    private long expiresIn;

    private final WebClient webClient = WebClient.create();

    public String getAccessToken() {
        return accessToken;
    }

    @PostConstruct
    public void init() {
        System.out.println("SpotifyTokenService init() called, attempting initial token refresh...");
        refreshAccessToken();
    }

    @Scheduled(fixedDelay = 2700000)
    public void refreshAccessToken() {
        System.out.println("SpotifyTokenService refreshAccessToken() called...");
        try {
            String basicAuth = Base64.getEncoder().encodeToString(
                    (clientId + ":" + clientSecret).getBytes(StandardCharsets.UTF_8)
            );

            Map<String, Object> response = webClient.post()
                    .uri(tokenUrl)
                    .header("Authorization", "Basic " + basicAuth)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .bodyValue("grant_type=client_credentials")
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                    .block();

            if (response != null && response.containsKey("access_token")) {
                accessToken = (String) response.get("access_token");
                expiresIn = ((Number) response.get("expires_in")).longValue();
                System.out.println("Spotify access token refreshed successfully: "
                        + accessToken.substring(0, Math.min(accessToken.length(), 10)) + "...");
            } else {
                System.err.println("Failed to refresh Spotify access token: response was null or missing access_token");
            }
        } catch (Exception e) {
            System.err.println("Exception while refreshing Spotify access token:");
            e.printStackTrace();
        }
    }
}
