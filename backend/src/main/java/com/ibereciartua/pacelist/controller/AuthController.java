package com.ibereciartua.pacelist.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.SessionAttributes;

import jakarta.servlet.http.HttpSession;

@Controller
@SessionAttributes("callbackUrl")
public class AuthController {

    private final OAuth2AuthorizedClientRepository authorizedClientRepository;

    public AuthController(OAuth2AuthorizedClientRepository authorizedClientRepository) {
        this.authorizedClientRepository = authorizedClientRepository;
    }


    @GetMapping("/auth/login")
    public String initiateSpotifyLogin(@RequestParam("redirectUri") String redirectUri, HttpSession session) {
        session.setAttribute("callbackUrl", redirectUri);
        return "redirect:/oauth2/authorization/spotify";
    }

    @GetMapping("/auth/logout")
    public ResponseEntity<Void> disconnect(@RegisteredOAuth2AuthorizedClient("spotify") OAuth2AuthorizedClient authorizedClient,
                                             Authentication authentication,
                                             HttpSession session,
                                             HttpServletRequest request,
                                             HttpServletResponse response) {
        if (authorizedClient != null && authentication != null) {
            authorizedClientRepository.removeAuthorizedClient(
                    authorizedClient.getClientRegistration().getRegistrationId(),
                    authentication,
                    request,
                    response);
        }
        session.invalidate();
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok().build();
    }

}
