package com.ibereciartua.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @GetMapping("/auth/login")
    public String initiateSpotifyLogin() {
        return "redirect:/oauth2/authorization/spotify";
    }
}
