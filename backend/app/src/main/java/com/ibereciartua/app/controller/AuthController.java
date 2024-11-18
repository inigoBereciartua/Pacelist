package com.ibereciartua.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller for handling authentication requests.
 * This controller is responsible for redirecting the user to the Spotify login page.
 */
@Controller
public class AuthController {

    @GetMapping("/auth/login")
    public String initiateSpotifyLogin() {
        return "redirect:/oauth2/authorization/spotify";
    }
}
