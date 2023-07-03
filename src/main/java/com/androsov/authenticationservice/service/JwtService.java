package com.androsov.authenticationservice.service;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class JwtService {
    @Value("${jwt.secret}")
    private String secret;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    private final String USERNAME = "username";
    private final String AUTHORITIES = "authorities";

    public String generateToken(String username, String authorities) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(USERNAME, username);
        claims.put(AUTHORITIES, authorities);

        return Jwts.builder()
                .signWith(signatureAlgorithm, secret)
                .setClaims(claims)
                .compact();
    }

    public boolean isValid(String jwtToken) {
        try {
            Jwts.parser().setSigningKey(secret).parse(jwtToken);
        } catch (JwtException e) {
            return false;
        }

        return true;
    }
}
