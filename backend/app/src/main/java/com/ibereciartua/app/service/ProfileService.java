package com.ibereciartua.app.service;

import com.ibereciartua.app.domain.exception.UserCredentialsNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * The ProfileService class is responsible for handling the profile requests.
 * This service is responsible for handling the profile requests and returning the profile information.
 */
@Service
public class ProfileService {

    private final AuthService authService;

    public ProfileService(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Get the profile information.
     * @return The profile information.
     */
    public String getProfile() {
        Optional<String> name = authService.getName();
        if (name.isEmpty()) {
            throw new UserCredentialsNotFoundException("No name found");
        }
        return name.get();
    }
}
