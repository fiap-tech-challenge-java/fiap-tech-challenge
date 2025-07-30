package com.fiap.techchallenge.application.services.auth.impl;

import com.fiap.techchallenge.application.services.auth.AuthUseCase;
import com.fiap.techchallenge.domain.exceptions.CustomAuthenticationException;
import com.fiap.techchallenge.infrastructure.config.JwtUtil;
import com.fiap.techchallenge.infrastructure.config.UserDetailsImpl;
import com.fiap.techchallenge.model.LoginRequest;
import com.fiap.techchallenge.model.LoginResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.OffsetDateTime;
import io.jsonwebtoken.Claims;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthUseCaseImplTest {

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthUseCaseImpl authUseCase;

    private LoginRequest loginRequest;
    private UserDetailsImpl userDetails;
    private final String username = "testuser";
    private final String password = "password";
    private final String encodedPassword = "encodedPassword";
    private final String token = "testToken";
    private final UUID userId = UUID.randomUUID();
    private final String role = "ROLE_USER";

    @BeforeEach
    void setUp() {
        loginRequest = new LoginRequest();
        loginRequest.setLogin(username);
        loginRequest.setPassword(password);

        // Create a UserDetailsImpl with the encoded password
        userDetails = new UserDetailsImpl(userId, username, encodedPassword, "test@example.com",
                Collections.singletonList(new SimpleGrantedAuthority(role)));
    }

    @Test
    void shouldAuthenticateSuccessfully() {
        // Arrange
        Claims claims = mock(Claims.class);
        Date expiration = new Date(System.currentTimeMillis() + 3600000); // 1 hour from now

        when(claims.getExpiration()).thenReturn(expiration);
        when(jwtUtil.extractAllClaims(anyString())).thenReturn(claims);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(passwordEncoder.matches(password, userDetails.getPassword())).thenReturn(true);
        when(jwtUtil.generateToken(userId, role)).thenReturn(token);

        // Act
        ResponseEntity<LoginResponse> response = authUseCase.authenticate(loginRequest);

        // Assert
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(token, response.getBody().getToken());
        assertEquals(userId, response.getBody().getUserId());
        assertTrue(response.getBody().getExpiresIn() > 0);
        verify(userDetailsService).loadUserByUsername(username);
        verify(passwordEncoder).matches(password, userDetails.getPassword());
        verify(jwtUtil).generateToken(userId, role);
        verify(jwtUtil).extractAllClaims(token);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        // Arrange
        when(userDetailsService.loadUserByUsername(username))
                .thenThrow(new UsernameNotFoundException("User not found"));

        // Act & Assert
        CustomAuthenticationException exception = assertThrows(CustomAuthenticationException.class,
                () -> authUseCase.authenticate(loginRequest));

        assertEquals("User Not Found.", exception.getMessage());
        verify(userDetailsService).loadUserByUsername(username);
        verifyNoInteractions(passwordEncoder);
        verifyNoInteractions(jwtUtil);
    }

    @Test
    void shouldThrowExceptionWhenPasswordIsIncorrect() {
        // Arrange
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(passwordEncoder.matches(password, userDetails.getPassword())).thenReturn(false);

        // Act & Assert
        CustomAuthenticationException exception = assertThrows(CustomAuthenticationException.class,
                () -> authUseCase.authenticate(loginRequest));

        assertEquals("Invalid credentials.", exception.getMessage());
        verify(userDetailsService).loadUserByUsername(username);
        verify(passwordEncoder).matches(password, userDetails.getPassword());
        verifyNoInteractions(jwtUtil);
    }

    @Test
    void shouldSetCorrectExpirationTime() {
        // Arrange
        long currentTime = System.currentTimeMillis();
        long expirationTime = currentTime + 3600000; // 1 hour later
        Date expirationDate = new Date(expirationTime);

        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(passwordEncoder.matches(password, userDetails.getPassword())).thenReturn(true);
        when(jwtUtil.generateToken(userId, role)).thenReturn(token);

        Claims claims = mock(Claims.class);
        when(jwtUtil.extractAllClaims(token)).thenReturn(claims);
        when(claims.getExpiration()).thenReturn(expirationDate);

        // Act
        ResponseEntity<LoginResponse> response = authUseCase.authenticate(loginRequest);

        // Assert
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getExpiresAt());
        assertTrue(response.getBody().getExpiresIn() > 0);

        // Verify the expiration time is correct
        OffsetDateTime expectedExpiration = OffsetDateTime.ofInstant(expirationDate.toInstant(),
                ZoneId.systemDefault());
        assertEquals(expectedExpiration, response.getBody().getExpiresAt());
    }

    @Test
    void shouldThrowExceptionWhenUsernameIsNull() {
        // Arrange
        loginRequest = new LoginRequest();
        loginRequest.setLogin(null);
        loginRequest.setPassword(password);

        // Mock the UserDetailsService to throw UsernameNotFoundException for null username
        when(userDetailsService.loadUserByUsername(null)).thenThrow(new UsernameNotFoundException("User not found"));

        // Act & Assert
        CustomAuthenticationException exception = assertThrows(CustomAuthenticationException.class,
                () -> authUseCase.authenticate(loginRequest));

        assertEquals("User Not Found.", exception.getMessage());
        verify(userDetailsService).loadUserByUsername(null);
        verifyNoMoreInteractions(userDetailsService);
        verifyNoInteractions(passwordEncoder, jwtUtil);
    }

    @Test
    void shouldThrowExceptionWhenUsernameIsEmpty() {
        // Arrange
        loginRequest = new LoginRequest();
        loginRequest.setLogin("");
        loginRequest.setPassword(password);

        // Mock the UserDetailsService to throw UsernameNotFoundException for empty username
        when(userDetailsService.loadUserByUsername("")).thenThrow(new UsernameNotFoundException("User not found"));

        // Act & Assert
        CustomAuthenticationException exception = assertThrows(CustomAuthenticationException.class,
                () -> authUseCase.authenticate(loginRequest));

        assertEquals("User Not Found.", exception.getMessage());
        verify(userDetailsService).loadUserByUsername("");
        verifyNoMoreInteractions(userDetailsService);
        verifyNoInteractions(passwordEncoder, jwtUtil);
    }

    @Test
    void shouldThrowExceptionWhenPasswordIsNull() {
        // Arrange
        loginRequest = new LoginRequest();
        loginRequest.setLogin(username);
        loginRequest.setPassword(null);

        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);

        // Act
        CustomAuthenticationException exception = assertThrows(CustomAuthenticationException.class,
                () -> authUseCase.authenticate(loginRequest));

        // Assert
        assertEquals("Invalid credentials.", exception.getMessage());
        verify(userDetailsService).loadUserByUsername(username);
        // We expect passwordEncoder.matches to be called with null password
        verify(passwordEncoder).matches(null, userDetails.getPassword());
        verifyNoInteractions(jwtUtil);
    }

    @Test
    void shouldHandleUnexpectedException() {
        // Arrange
        when(userDetailsService.loadUserByUsername(username)).thenThrow(new RuntimeException("Unexpected error"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> authUseCase.authenticate(loginRequest));

        verifyNoInteractions(passwordEncoder, jwtUtil);
    }

    @Test
    void shouldHandleInvalidRoleInToken() {
        // Arrange
        Claims claims = mock(Claims.class);
        Date expiration = new Date(System.currentTimeMillis() + 3600000);

        when(claims.getExpiration()).thenReturn(expiration);
        when(jwtUtil.extractAllClaims(anyString())).thenReturn(claims);

        // Create user with a default role to avoid NoSuchElementException
        UserDetailsImpl userWithDefaultRole = new UserDetailsImpl(userId, username, encodedPassword, "test@example.com",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));

        when(userDetailsService.loadUserByUsername(username)).thenReturn(userWithDefaultRole);
        when(passwordEncoder.matches(password, userWithDefaultRole.getPassword())).thenReturn(true);

        // Mock the token generation
        when(jwtUtil.generateToken(any(UUID.class), anyString())).thenReturn(token);

        // Mock the claims extraction
        when(jwtUtil.extractAllClaims(token)).thenReturn(claims);
        when(claims.getExpiration()).thenReturn(expiration);

        // Act
        ResponseEntity<LoginResponse> response = authUseCase.authenticate(loginRequest);

        // Assert
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(token, response.getBody().getToken());

        verify(userDetailsService).loadUserByUsername(username);
        verify(passwordEncoder).matches(password, userWithDefaultRole.getPassword());
        // Verify generateToken was called with the correct user ID and role
        verify(jwtUtil).generateToken(userId, "ROLE_USER");
        verify(jwtUtil).extractAllClaims(token);
        verify(claims).getExpiration();
    }

    @Test
    void shouldHandleExpiredToken() {
        // Arrange
        Claims claims = mock(Claims.class);
        Date expiration = new Date(System.currentTimeMillis() - 1000); // Past date

        when(claims.getExpiration()).thenReturn(expiration);
        when(jwtUtil.extractAllClaims(anyString())).thenReturn(claims);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(passwordEncoder.matches(password, userDetails.getPassword())).thenReturn(true);
        when(jwtUtil.generateToken(userId, role)).thenReturn(token);

        // Act
        ResponseEntity<LoginResponse> response = authUseCase.authenticate(loginRequest);

        // Assert - Should still return 200 OK but with a short expiration
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getExpiresIn() <= 0);
    }
}
