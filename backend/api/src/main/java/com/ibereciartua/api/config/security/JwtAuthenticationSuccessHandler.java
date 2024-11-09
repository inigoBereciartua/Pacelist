package com.ibereciartua.api.config.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.core.Authentication;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JwtAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final JwtUtil jwtUtil;
    private final OAuth2AuthorizedClientService authorizedClientService;
    private final String successRedirectUrl;

    public JwtAuthenticationSuccessHandler(JwtUtil jwtUtil, OAuth2AuthorizedClientService authorizedClientService, String successRedirectUrl) {
        this.jwtUtil = jwtUtil;
        this.authorizedClientService = authorizedClientService;
        this.successRedirectUrl = successRedirectUrl;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        OAuth2AuthorizedClient authorizedClient = authorizedClientService.loadAuthorizedClient("spotify", authentication.getName());
        if (authorizedClient != null) {
            OAuth2AccessToken accessToken = authorizedClient.getAccessToken();
            OAuth2RefreshToken refreshToken = authorizedClient.getRefreshToken();

            Map<String, Object> claims = new HashMap<>();
            claims.put("username", authentication.getName());
            claims.put("roles", authentication.getAuthorities());
            claims.put("accessToken", accessToken.getTokenValue());
            claims.put("refreshToken", refreshToken != null ? refreshToken.getTokenValue() : null);

            String jwt = jwtUtil.generateToken(claims, authentication.getName());
            String redirectUrl = successRedirectUrl + "?token=" + jwt;
            response.sendRedirect(redirectUrl);
        }
    }
}