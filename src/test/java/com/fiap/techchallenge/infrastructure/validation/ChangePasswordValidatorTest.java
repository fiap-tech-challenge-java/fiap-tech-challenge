package com.fiap.techchallenge.infrastructure.validation;

import com.fiap.techchallenge.application.ports.in.user.dtos.ChangePassword;
import com.fiap.techchallenge.domain.exceptions.InvalidPasswordPatternException;
import com.fiap.techchallenge.domain.exceptions.InvalidPreviousPasswordException;
import com.fiap.techchallenge.domain.exceptions.PasswordsDoNotMatchException;
import com.fiap.techchallenge.domain.utils.PasswordValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ChangePasswordValidatorTest {
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private ChangePasswordValidator validator;

    private ChangePassword changePassword;
    private String storedEncodedPassword;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        validator = new ChangePasswordValidator(passwordEncoder);
        changePassword = new ChangePassword();
        changePassword.setId(UUID.randomUUID());
        changePassword.setLastPassword("oldPass");
        changePassword.setNewPassword("NewPassword123!");
        changePassword.setConfirmPassword("NewPassword123!");
        storedEncodedPassword = "encodedOldPass";
    }

    @Test
    void shouldThrowExceptionWhenRequiredFieldsAreBlank() {
        changePassword.setId(null);
        Exception ex = assertThrows(InvalidPreviousPasswordException.class, () -> validator.isValid(changePassword, storedEncodedPassword));
        assertEquals("Invalid previous password.", ex.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenPasswordsDoNotMatch() {
        changePassword.setConfirmPassword("DifferentPassword");
        Exception ex = assertThrows(PasswordsDoNotMatchException.class, () -> validator.isValid(changePassword, storedEncodedPassword));
        assertEquals("Passwords do not match.", ex.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenLastPasswordIsInvalid() {
        when(passwordEncoder.matches("oldPass", storedEncodedPassword)).thenReturn(false);
        Exception ex = assertThrows(InvalidPreviousPasswordException.class, () -> validator.isValid(changePassword, storedEncodedPassword));
        assertEquals("Invalid previous password.", ex.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenNewPasswordPatternIsInvalid() {
        when(passwordEncoder.matches("oldPass", storedEncodedPassword)).thenReturn(true);
        mockStatic(PasswordValidator.class).when(() -> PasswordValidator.isValid("NewPassword123!")).thenReturn(false);
        Exception ex = assertThrows(InvalidPasswordPatternException.class, () -> validator.isValid(changePassword, storedEncodedPassword));
        assertEquals("The password must be at least 5 characters long, containing at least 1 uppercase letter and 1 number.", ex.getMessage());
    }
}
