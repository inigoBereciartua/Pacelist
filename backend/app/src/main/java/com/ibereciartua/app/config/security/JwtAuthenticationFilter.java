package com.ibereciartua.app.config.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final RequestMatcher apiMatcher;

    public JwtAuthenticationFilter(final JwtUtil jwtUtil, final String pathMatch) {
        this.jwtUtil = jwtUtil;
        this.apiMatcher = new AntPathRequestMatcher(pathMatch);
    }

    @Override
    protected boolean shouldNotFilter(@NonNull final HttpServletRequest request) {
        return !apiMatcher.matches(request);
    }

    @Override
    protected void doFilterInternal(@NonNull final HttpServletRequest request, @NonNull final HttpServletResponse response, @NonNull final FilterChain filterChain) throws IOException, ServletException {
        String jwt = extractJwtFromRequest(request);
        if (jwt != null && !jwtUtil.validateJwt(jwt)) {
            logger.error("Invalid authorization token");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid authorization token");
            return;
        }

        Claims claims = jwtUtil.getClaims(jwt);

        String username = claims.getSubject();
        List<String> roles = claims.get("roles", List.class);
        String accessToken = claims.get("accessToken", String.class);
        String refreshToken = claims.get("refreshToken", String.class);
        String authenticationProvider = claims.get("authenticationProvider", String.class);

        List<SimpleGrantedAuthority> authorities = roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        CustomUserDetails customUserDetails = new CustomUserDetails(username, accessToken, refreshToken, authenticationProvider, authorities);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(customUserDetails, null, authorities);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }

    private String extractJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        return (bearerToken != null && bearerToken.startsWith("Bearer ")) ? bearerToken.substring(7) : null;
    }
}
