package com.ibereciartua.app.service;

import com.ibereciartua.app.config.security.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * The AuthService class is responsible for handling the authentication requests.
 * This service is responsible for handling the authentication requests and returning the authentication information.
 * The authentication information includes the access token, the name and the authenticator provider.
 */
@Service
public class AuthService {

    private Optional<CustomUserDetails> getUserContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            return Optional.of((CustomUserDetails) authentication.getPrincipal());
        }
        return Optional.empty();
    }

    public Optional<String> getAccessToken() {
        Optional<CustomUserDetails> userContext = getUserContext();
        return userContext.map(CustomUserDetails::getAccessToken);
    }

    public Optional<String> getName() {
        Optional<CustomUserDetails> userContext = getUserContext();
        return userContext.map(CustomUserDetails::getUsername);
    }

    public Optional<String> getAuthenticatorProvider() {
        Optional<CustomUserDetails> userContext = getUserContext();
        return userContext.map(CustomUserDetails::getAuthenticationProvider);
    }
}
