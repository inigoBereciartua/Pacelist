package com.ibereciartua.api.service;

import org.springframework.stereotype.Service;

@Service
public class ProfileService {

    private final AuthService authService;

    public ProfileService(AuthService authService) {
        this.authService = authService;
    }

    public String getProfile() {
        return authService.getName();
    }
}
