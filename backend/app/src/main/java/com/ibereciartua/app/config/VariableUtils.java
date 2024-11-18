package com.ibereciartua.app.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Utility class for retrieving application variables.
 * This class is responsible for retrieving the application variables from the application properties file.
 */
@Component
public class VariableUtils {

    @Value("${app.allowed-origins}")
    private List<String> allowedOrigins;

    @Value("${app.jwt-secret-key}")
    private String jwtSecretKey;

    @Value("${app.login-redirect-url}")
    private String loginRedirectUrl;

    @Value("${spring.security.oauth2.client.registration.spotify.client-id}")
    private String spotifyClientId;

    @Value("${spring.security.oauth2.client.registration.spotify.client-secret}")
    private String spotifyClientSecret;

    @Value("${spring.security.oauth2.client.registration.spotify.redirect-uri}")
    private String spotifyRedirectUri;

    public List<String> getAllowedOrigins() {
        List<String> allOrigins = allowedOrigins;
        allOrigins.addAll(oAuthOrigins());
        return allOrigins;
    }

    private List<String> oAuthOrigins() {
        return List.of("https://accounts.spotify.com");
    }

    public String getJwtSecretKey() {
        return jwtSecretKey;
    }

    public String getLoginRedirectUrl() {
        return loginRedirectUrl;
    }

    public String getSpotifyClientId() {
        return spotifyClientId;
    }

    public String getSpotifyClientSecret() {
        return spotifyClientSecret;
    }

    public String getSpotifyRedirectUri() {
        return spotifyRedirectUri;
    }
}
