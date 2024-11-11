package com.ibereciartua.app.config.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.core.Authentication;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        String authenticationProvider = oauthToken.getAuthorizedClientRegistrationId();
        OAuth2AuthorizedClient authorizedClient = authorizedClientService.loadAuthorizedClient(authenticationProvider, authentication.getName());
        if (authorizedClient != null) {
            OAuth2AccessToken accessToken = authorizedClient.getAccessToken();
            OAuth2RefreshToken refreshToken = authorizedClient.getRefreshToken();

            Map<String, Object> claims = new HashMap<>();
            List<String> grantedAuthorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
            claims.put("username", authentication.getName());
            claims.put("roles", grantedAuthorities);
            claims.put("accessToken", accessToken.getTokenValue());
            claims.put("refreshToken", refreshToken != null ? refreshToken.getTokenValue() : null);
            claims.put("authenticationProvider", authenticationProvider);

            String jwt = jwtUtil.generateToken(claims, authentication.getName());
            String redirectUrl = successRedirectUrl + "?token=" + jwt;
            response.sendRedirect(redirectUrl);
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid authorization token");
        }
    }
}