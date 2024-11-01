package com.ibereciartua.pacelist.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.SessionAttributes;

import jakarta.servlet.http.HttpSession;

@Controller
@SessionAttributes("callbackUrl")
public class AuthController {

    @GetMapping("/auth/login")
    public String initiateSpotifyLogin(@RequestParam("redirectUri") String redirectUri, HttpSession session) {
        session.setAttribute("callbackUrl", redirectUri);
        return "redirect:/oauth2/authorization/spotify";
    }
}
