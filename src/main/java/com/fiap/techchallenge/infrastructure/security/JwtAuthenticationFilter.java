package com.fiap.techchallenge.infrastructure.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.techchallenge.adapters.in.rest.exception.ErrorResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.time.LocalDateTime;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtUtil jwtUtil;
    private final UserDetailsService uds;
    private final ObjectMapper objectMapper;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserDetailsService uds) {
        this.jwtUtil = jwtUtil;
        this.uds = uds;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {

        try {
            String header = req.getHeader("Authorization");

            if (header != null && header.startsWith("Bearer ")) {
                String token = header.substring(7);

                if (jwtUtil.validateToken(token)) {
                    String username = jwtUtil.extractUsername(token);
                    UserDetails ud = uds.loadUserByUsername(username);

                    var auth = new UsernamePasswordAuthenticationToken(ud, null, ud.getAuthorities());
                    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
                    SecurityContextHolder.getContext().setAuthentication(auth);

                    logger.debug("Usuário autenticado: {}", username);
                }
            }

            chain.doFilter(req, res);

        } catch (ExpiredJwtException ex) {
            logger.warn("Token JWT expirado: {}", ex.getMessage());
            handleJwtException(res, "Token expirado", "TOKEN_EXPIRED", HttpStatus.UNAUTHORIZED);

        } catch (MalformedJwtException ex) {
            logger.warn("Token JWT malformado: {}", ex.getMessage());
            handleJwtException(res, "Token malformado", "TOKEN_MALFORMED", HttpStatus.UNAUTHORIZED);

        } catch (UnsupportedJwtException ex) {
            logger.warn("Token JWT não suportado: {}", ex.getMessage());
            handleJwtException(res, "Token não suportado", "TOKEN_UNSUPPORTED", HttpStatus.UNAUTHORIZED);

        } catch (IllegalArgumentException ex) {
            logger.warn("Token JWT com argumento ilegal: {}", ex.getMessage());
            handleJwtException(res, "Token inválido", "TOKEN_INVALID", HttpStatus.UNAUTHORIZED);

        } catch (UsernameNotFoundException ex) {
            logger.warn("Usuário não encontrado: {}", ex.getMessage());
            handleJwtException(res, "Usuário não encontrado", "USER_NOT_FOUND", HttpStatus.UNAUTHORIZED);

        } catch (Exception ex) {
            logger.error("Erro inesperado no filtro JWT: {}", ex.getMessage(), ex);
            handleJwtException(res, "Erro interno de autenticação", "AUTHENTICATION_ERROR",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void handleJwtException(HttpServletResponse response, String message, String errorCode, HttpStatus status)
            throws IOException {

        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ErrorResponse errorResponse = new ErrorResponse(message, errorCode, status.value(), null);
        errorResponse.setTimestamp(LocalDateTime.now());

        String jsonResponse = objectMapper.writeValueAsString(errorResponse);
        response.getWriter().write(jsonResponse);
    }

}
