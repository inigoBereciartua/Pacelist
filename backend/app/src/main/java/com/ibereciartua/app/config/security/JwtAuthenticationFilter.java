package com.ibereciartua.app.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(@NonNull final HttpServletRequest request, @NonNull final HttpServletResponse response, @NonNull final FilterChain filterChain) throws IOException, ServletException {
        String jwt = extractJwtFromRequest(request);
        logger.info("JWT: " + jwt);
        if (jwt != null && !jwtUtil.validateJwt(jwt)) {
            logger.error("Invalid authorization token");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid authorization token");
            return;
        }
        logger.info("Valid authorization token");

        Claims claims = jwtUtil.getClaims(jwt);

        String username = claims.getSubject();
        List<String> roles = claims.get("roles", List.class);
        String accessToken = claims.get("accessToken", String.class);
        String refreshToken = claims.get("refreshToken", String.class);
        String authenticationProvider = claims.get("authenticationProvider", String.class);

        List<SimpleGrantedAuthority> authorities = roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        CustomUserDetails customUserDetails = new CustomUserDetails(username, accessToken, refreshToken, "", authorities);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(customUserDetails, null, authorities);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        Authentication newAuthentication = SecurityContextHolder.getContext().getAuthentication();
        logger.info("User " + newAuthentication.getName() + " authenticated: " + newAuthentication.isAuthenticated());
        filterChain.doFilter(request, response);
    }

    private String extractJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        return (bearerToken != null && bearerToken.startsWith("Bearer ")) ? bearerToken.substring(7) : null;
    }
}
