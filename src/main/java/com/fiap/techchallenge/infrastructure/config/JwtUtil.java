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

    public String generateToken(UUID userId, String role) {
        Date now = new Date();
        return Jwts.builder()
                .claim("userId", userId)
                .claim("role", role)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + expMs))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public Claims extractAllClaims(String token) {
        try {
            return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException ex) {
            logger.warn("Token expirado ao extrair claims: {}", ex.getMessage());
            throw ex;
        } catch (MalformedJwtException ex) {
            logger.warn("Token malformado ao extrair claims: {}", ex.getMessage());
            throw ex;
        } catch (UnsupportedJwtException ex) {
            logger.warn("Token não suportado ao extrair claims: {}", ex.getMessage());
            throw ex;
        } catch (IllegalArgumentException ex) {
            logger.warn("Token com argumento ilegal ao extrair claims: {}", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            logger.error("Erro inesperado ao extrair claims do token: {}", ex.getMessage(), ex);
            throw new JwtException("Erro ao processar token", ex);
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
            logger.warn("Token expirado: {}", ex.getMessage());
            throw ex;
        } catch (MalformedJwtException ex) {
            logger.warn("Token malformado: {}", ex.getMessage());
            throw ex;
        } catch (UnsupportedJwtException ex) {
            logger.warn("Token não suportado: {}", ex.getMessage());
            throw ex;
        } catch (IllegalArgumentException ex) {
            logger.warn("Token com argumento ilegal: {}", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            logger.error("Erro inesperado ao validar token: {}", ex.getMessage(), ex);
            throw new JwtException("Erro ao validar token", ex);
        }
    }

    public boolean isTokenExpired(String token) {
        try {
            Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();

            return claims.getExpiration().before(new Date());
        } catch (Exception ex) {
            logger.warn("Erro ao verificar expiração do token: {}", ex.getMessage());
            return true; // Considera como expirado em caso de erro
        }
    }
}
