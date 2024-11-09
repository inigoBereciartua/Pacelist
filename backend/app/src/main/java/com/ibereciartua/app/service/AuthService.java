package com.ibereciartua.app.service;

import com.ibereciartua.app.config.security.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private Optional<CustomUserDetails> getUserContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            return Optional.of((CustomUserDetails) authentication.getPrincipal());
        }
        return Optional.empty();
    }

    public String getAccessToken() {
        Optional<CustomUserDetails> userContext = getUserContext();
        return userContext.map(CustomUserDetails::getAccessToken).orElse(null);
    }

    public String getName() {
        Optional<CustomUserDetails> userContext = getUserContext();
        return userContext.map(CustomUserDetails::getUsername).orElse(null);
    }
}
