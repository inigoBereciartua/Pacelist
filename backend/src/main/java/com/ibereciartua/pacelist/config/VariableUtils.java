package com.ibereciartua.pacelist.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class VariableUtils {

    @Value("${app.allowed-origins}")
    private List<String> allowedOrigins;

    @Value("${spring.security.oauth2.client.registration.spotify.client-id}")
    private String spotifyClientId;

    @Value("${spring.security.oauth2.client.registration.spotify.client-secret}")
    private String spotifyClientSecret;

    @Value("${spring.security.oauth2.client.registration.spotify.redirect-uri}")
    private String spotifyRedirectUri;

    public List<String> getAllowedOrigins() {
        return allowedOrigins;
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
