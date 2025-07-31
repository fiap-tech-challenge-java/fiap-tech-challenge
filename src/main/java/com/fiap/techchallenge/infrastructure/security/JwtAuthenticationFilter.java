package com.fiap.techchallenge.infrastructure.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.techchallenge.infrastructure.config.JwtUtil;
import com.fiap.techchallenge.model.ErrorResponse;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtUtil jwtUtil;
    private final ExtendedUserDetailsService uds;
    private final ObjectMapper objectMapper;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, ExtendedUserDetailsService uds) {
        this.jwtUtil = jwtUtil;
        this.uds = uds;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {

        try {
            // Processa o token JWT apenas se estiver presente
            String header = req.getHeader("Authorization");
            if (header == null || !header.startsWith("Bearer ")) {
                chain.doFilter(req, res);
                return;
            }

            String token = header.substring(7);
            if (jwtUtil.validateToken(token)) {
                UUID userId = jwtUtil.extractUserId(token);
                UserDetails ud = uds.loadUserById(userId);

                var auth = new UsernamePasswordAuthenticationToken(ud, null, ud.getAuthorities());
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
                SecurityContextHolder.getContext().setAuthentication(auth);

                logger.debug("Authenticated user: {}", userId);
            }

            chain.doFilter(req, res);

        } catch (ExpiredJwtException ex) {
            logger.warn("Expired JWT token: {}", ex.getMessage());
            handleJwtException(res, "Expired token:", "TOKEN_EXPIRED", HttpStatus.UNAUTHORIZED);

        } catch (MalformedJwtException ex) {
            logger.warn("Malformed JWT token: {}", ex.getMessage());
            handleJwtException(res, "Malformed token:", "TOKEN_MALFORMED", HttpStatus.UNAUTHORIZED);

        } catch (UnsupportedJwtException ex) {
            logger.warn("Unsupported JWT token: {}", ex.getMessage());
            handleJwtException(res, "Unsupported token", "TOKEN_UNSUPPORTED", HttpStatus.UNAUTHORIZED);

        } catch (IllegalArgumentException ex) {
            logger.warn("JWT token with illegal argument: {}", ex.getMessage());
            handleJwtException(res, "Invalid token", "TOKEN_INVALID", HttpStatus.UNAUTHORIZED);

        } catch (UsernameNotFoundException ex) {
            logger.warn("User not found: {}", ex.getMessage());
            handleJwtException(res, "User not found", "USER_NOT_FOUND", HttpStatus.UNAUTHORIZED);

        } catch (Exception ex) {
            logger.error("Unexpected error in JWT filter: {}", ex.getMessage(), ex);
            handleJwtException(res, "Internal authentication error", "AUTHENTICATION_ERROR",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    private void handleJwtException(HttpServletResponse response, String message, String errorCode, HttpStatus status)
            throws IOException {

        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(message);
        errorResponse.setCode(errorCode);
        errorResponse.setStatus(status.value());

        String jsonResponse = objectMapper.writeValueAsString(errorResponse);
        response.getWriter().write(jsonResponse);
    }
}
