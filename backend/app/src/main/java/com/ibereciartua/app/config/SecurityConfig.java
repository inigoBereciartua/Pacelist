package com.ibereciartua.app.config;

import com.ibereciartua.app.config.security.JwtAuthenticationFilter;
import com.ibereciartua.app.config.security.JwtAuthenticationSuccessHandler;
import com.ibereciartua.app.config.security.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * Security configuration for the application.
 * This class configures the security settings for the application.
 * It defines the security filter chain and the authentication success handler.
 * It also configures the CORS policy for the application.
 */
@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private final OAuth2AuthorizedClientService authorizedClientService;
    private final JwtUtil jwtUtil;
    private final VariableUtils variableUtils;

    private final String API_AUTHENTICATED_PATH = "/api/**";

    public SecurityConfig(final @Lazy OAuth2AuthorizedClientService authorizedClientService, final JwtUtil jwtUtil, VariableUtils variableUtils) {
        this.authorizedClientService = authorizedClientService;
        this.jwtUtil = jwtUtil;
        this.variableUtils = variableUtils;
    }

    @Bean
    public JwtAuthenticationSuccessHandler jwtAuthenticationSuccessHandler() {
        return new JwtAuthenticationSuccessHandler(jwtUtil, authorizedClientService, variableUtils.getLoginRedirectUrl());
    }

    /**
     * Configures the security filter chain for the application.
     * @param http the HttpSecurity object to configure the security filter chain
     * @return the SecurityFilterChain object
     * @throws Exception if an error occurs while configuring the security filter chain
     * @see SecurityFilterChain
     * @see HttpSecurity
     * @see JwtAuthenticationFilter
     * @see JwtAuthenticationSuccessHandler
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/auth/**", "/oauth2/**", "/login/**").permitAll()
                        .requestMatchers(org.springframework.http.HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(API_AUTHENTICATED_PATH).authenticated()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        .successHandler(jwtAuthenticationSuccessHandler())
                )
                .addFilterBefore(new JwtAuthenticationFilter(jwtUtil, API_AUTHENTICATED_PATH), UsernamePasswordAuthenticationFilter.class)
                .csrf(AbstractHttpConfigurer::disable)
                .cors(withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    /**
     * Configures the CORS policy for the application.
     * This method defines the allowed origins, headers, and methods for the application.
     * @return the CorsConfigurationSource object
     * @see CorsConfiguration
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(variableUtils.getAllowedOrigins());
        config.setAllowedHeaders(Collections.singletonList("*"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
