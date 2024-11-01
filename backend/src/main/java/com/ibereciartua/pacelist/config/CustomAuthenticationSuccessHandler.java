package com.ibereciartua.pacelist.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        AuthenticationSuccessHandler.super.onAuthenticationSuccess(request, response, chain, authentication);
    }

    @Override
    public void onAuthenticationSuccess(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response, Authentication authentication) throws IOException {
        HttpSession session = request.getSession();
        DefaultSavedRequest initialRequest = (DefaultSavedRequest) session.getAttribute("SPRING_SECURITY_SAVED_REQUEST");
        String requestQuery = initialRequest.getQueryString();
        String[] queryParts = requestQuery.split("&");
        String callbackUri = null;
        for (String queryPart : queryParts) {
            if (queryPart.contains("redirectUri")) {
                callbackUri = queryPart.split("=")[1];
                break;
            }
        }
        if (callbackUri != null) {
            session.removeAttribute("redirectUri");
            response.sendRedirect(callbackUri);
        } else {
            response.sendRedirect("/default-success-url");  // Fallback URL if no callback is provided (?)
        }
    }
}
