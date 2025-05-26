package org.example.charityapp.services;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

@Component
public class JwtUtil {

    private final String SECRET = "your-secret-key"; // Change in production
    private final long EXPIRATION_TIME = 864_000_000; // 10 days
    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    public String generateToken(UserDetails userDetails) {
        return JWT.create()
                .withSubject(userDetails.getUsername())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .withClaim("roles", userDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()))
                .sign(Algorithm.HMAC256(SECRET));
    }

    public String validateTokenAndGetUsername(String token) {
        try {
            return JWT.require(Algorithm.HMAC256(SECRET))
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (Exception e) {
            logger.error("Error validating token: " + e.getMessage());
            return null;
        }
    }

    public List<String> getRoles(String token) {
        try {
            return JWT.require(Algorithm.HMAC256(SECRET))
                    .build()
                    .verify(token)
                    .getClaim("roles")
                    .asList(String.class);
        } catch (Exception e) {
            logger.error("Error getting roles from token: " + e.getMessage());
            return List.of();
        }
    }
}