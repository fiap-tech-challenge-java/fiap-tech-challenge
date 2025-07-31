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
        Exception ex = assertThrows(InvalidPreviousPasswordException.class,
                () -> validator.isValid(changePassword, storedEncodedPassword));
        assertEquals("Invalid previous password.", ex.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenPasswordsDoNotMatch() {
        changePassword.setConfirmPassword("DifferentPassword");
        Exception ex = assertThrows(PasswordsDoNotMatchException.class,
                () -> validator.isValid(changePassword, storedEncodedPassword));
        assertEquals("Passwords do not match.", ex.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenLastPasswordIsInvalid() {
        try (var mocked = mockStatic(PasswordValidator.class)) {
            mocked.when(() -> PasswordValidator.isValid(changePassword.getNewPassword())).thenReturn(true);
            when(passwordEncoder.matches(changePassword.getLastPassword(), storedEncodedPassword)).thenReturn(false);
            Exception ex = assertThrows(InvalidPreviousPasswordException.class,
                    () -> validator.isValid(changePassword, storedEncodedPassword));
            assertEquals("Invalid previous password.", ex.getMessage());
        }
    }

    @Test
    void shouldValidateSuccessfullyWhenAllFieldsAreValid() {
        try (var mocked = mockStatic(PasswordValidator.class)) {
            mocked.when(() -> PasswordValidator.isValid(changePassword.getNewPassword())).thenReturn(true);
            when(passwordEncoder.matches(changePassword.getLastPassword(), storedEncodedPassword)).thenReturn(true);
            assertDoesNotThrow(() -> validator.isValid(changePassword, storedEncodedPassword));
        }
    }
}
