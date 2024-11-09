package com.ibereciartua.app.config.security;

import com.ibereciartua.app.config.VariableUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

@Component
public class JwtUtil {

    private final VariableUtils variableUtils;

    public JwtUtil(VariableUtils variableUtils) {
        this.variableUtils = variableUtils;
    }

    public String generateToken(final Map<String, Object> claims, final String subject) {
        long jwtExpirationMs = 1800000; // 30 minutes

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, variableUtils.getJwtSecretKey())
                .compact();
    }

    public boolean validateJwt(String token) {
        try {
            Jwts.parser().setSigningKey(variableUtils.getJwtSecretKey()).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(variableUtils.getJwtSecretKey())
                .parseClaimsJws(token)
                .getBody();
    }
}
