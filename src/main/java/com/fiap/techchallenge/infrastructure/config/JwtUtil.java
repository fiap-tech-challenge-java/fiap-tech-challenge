package com.fiap.techchallenge.infrastructure.config;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Component
public class JwtUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    @Value(value = "${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration-ms}")
    private long expMs;

    public String generateToken(String username, UUID userId, String email) {
        Date now = new Date();
        return Jwts.builder().setSubject(username).claim("userId", userId).claim("email", email).setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + expMs)).signWith(SignatureAlgorithm.HS256, secret).compact();
    }

    public Claims extractAllClaims(String token) {
        try {
            return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException ex) {
            logger.warn("Token expired while extracting claims: {}", ex.getMessage());
            throw ex;
        } catch (MalformedJwtException ex) {
            logger.warn("Malformed token while extracting claims: {}", ex.getMessage());
            throw ex;
        } catch (UnsupportedJwtException ex) {
            logger.warn("Unsupported token while extracting claims: {}", ex.getMessage());
            throw ex;
        } catch (IllegalArgumentException ex) {
            logger.warn("Token with illegal argument while extracting claims: {}", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            logger.error("Unexpected error while extracting claims from the token: {}", ex.getMessage(), ex);
            throw new JwtException("Error while processing token.", ex);
        }
    }

    public String extractUsername(String token) {
        Claims claims = extractAllClaims(token);
        return claims.getSubject();
    }

    public String extractUserId(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("userId", String.class);
    }

    public String extractEmail(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("email", String.class);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException ex) {
            logger.warn("Token expired: {}", ex.getMessage());
            throw ex;
        } catch (MalformedJwtException ex) {
            logger.warn("Malformed token: {}", ex.getMessage());
            throw ex;
        } catch (UnsupportedJwtException ex) {
            logger.warn("Unsupported token: {}", ex.getMessage());
            throw ex;
        } catch (IllegalArgumentException ex) {
            logger.warn("Token with illegal argument: {}", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            logger.error("Unexpected error while validating token: {}", ex.getMessage(), ex);
            throw new JwtException("Error while validating token.", ex);
        }
    }

    public boolean isTokenExpired(String token) {
        try {
            Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();

            return claims.getExpiration().before(new Date());
        } catch (Exception ex) {
            logger.warn("Error while checking token expiration: {}", ex.getMessage());
            return true; // Considera como expirado em caso de erro
        }
    }
}
