package com.ibereciartua.app.service;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProfileService {

    private final AuthService authService;

    public ProfileService(AuthService authService) {
        this.authService = authService;
    }

    public String getProfile() {
        Optional<String> name = authService.getName();
        if (name.isEmpty()) {
            throw new RuntimeException("No name found");
        }
        return name.get();
    }
}
