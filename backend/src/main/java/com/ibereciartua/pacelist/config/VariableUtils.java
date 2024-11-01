package com.ibereciartua.pacelist.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class VariableUtils {

    @Value("${app.current-domain}")
    private String currentDomain;

    @Value("${spring.security.oauth2.client.registration.spotify.client-id}")
    private String spotifyClientId;

    @Value("${spring.security.oauth2.client.registration.spotify.client-secret}")
    private String spotifyClientSecret;

    @Value("${spring.security.oauth2.client.registration.spotify.redirect-uri}")
    private String spotifyRedirectUri;

    public String getCurrentDomain() {
        return currentDomain;
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
