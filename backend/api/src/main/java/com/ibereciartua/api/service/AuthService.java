package com.ibereciartua.api.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final OAuth2AuthorizedClientService authorizedClientService;

    public AuthService(OAuth2AuthorizedClientService authorizedClientService) {
        this.authorizedClientService = authorizedClientService;
    }

    private OAuth2AuthenticationToken getOAuth2AuthenticationToken() {
        return (OAuth2AuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
    }

    public String getAccessToken() {
        OAuth2AuthenticationToken authentication = getOAuth2AuthenticationToken();
        OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(
                authentication.getAuthorizedClientRegistrationId(),
                authentication.getName());
        return client.getAccessToken().getTokenValue();
    }

    public String getName() {
        OAuth2AuthenticationToken authentication = getOAuth2AuthenticationToken();
        return authentication.getName();
    }


}
