package com.ibereciartua.app.controller;

import com.ibereciartua.app.service.ProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The ProfileController class is responsible for handling the profile requests.
 * This controller is responsible for handling the profile requests and returning the profile information.
 */
@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    /**
     * Get the profile information.
     * @return The profile information.
     */
    @GetMapping
    public ResponseEntity<String> getProfile() {
        String profile = profileService.getProfile();
        return ResponseEntity.ok(profile);
    }
}
