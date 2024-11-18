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

/**
 * Authentication success handler for the application.
 * This class is responsible for handling the successful authentication events.
 * It generates a JWT token and redirects the user to the success redirect URL.
 * @see AuthenticationSuccessHandler
 * @see JwtUtil
 * @see OAuth2AuthorizedClientService
 * @see OAuth2AuthorizedClient
 * @see OAuth2AccessToken
 * @see OAuth2RefreshToken
 * @see Authentication
 */
public class JwtAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final JwtUtil jwtUtil;
    private final OAuth2AuthorizedClientService authorizedClientService;
    private final String successRedirectUrl;

    public JwtAuthenticationSuccessHandler(JwtUtil jwtUtil, OAuth2AuthorizedClientService authorizedClientService, String successRedirectUrl) {
        this.jwtUtil = jwtUtil;
        this.authorizedClientService = authorizedClientService;
        this.successRedirectUrl = successRedirectUrl;
    }

    /**
     * Handles the successful authentication event.
     * This method generates a JWT token and redirects the user to the success redirect URL.
     * @param request the request which caused the successful authentication
     * @param response the response
     * @param authentication the <tt>Authentication</tt> object which was created during
     * the authentication process.
     * @throws IOException if an error occurs while handling the successful authentication event
     */
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