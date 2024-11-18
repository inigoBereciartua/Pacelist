package com.ibereciartua.app.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

/**
 * Listener for handling successful authentication events.
 * This listener is responsible for logging the successful authentication events.
 */
@Component
public class AuthenticationSuccessListener implements ApplicationListener<AuthenticationSuccessEvent> {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationSuccessListener.class);

    /**
     * Handles the successful authentication event.
     * @param event the authentication success event
     */
    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        Object principal = event.getAuthentication().getPrincipal();
        if (principal instanceof OAuth2User user) {
            String userName = user.getAttribute("id");
            logger.info("User {} successfully logged in with Spotify", userName);
        }
    }
}
