package com.ibereciartua.app.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @GetMapping("/auth/login")
    public String initiateSpotifyLogin() {
        return "redirect:/oauth2/authorization/spotify";
    }
}
