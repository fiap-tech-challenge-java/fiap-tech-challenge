package com.fiap.techchallenge.infrastructure.validation;

import com.fiap.techchallenge.application.ports.in.user.dtos.CreateUser;
import com.fiap.techchallenge.domain.utils.UsernameValidator;
import com.fiap.techchallenge.infrastructure.adapters.out.persistence.user.repositories.UserJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

class CreateUserValidatorTest {
    @Mock
    private UserJpaRepository userJpaRepository;
    @Mock
    private UsernameValidator usernameValidator;

    @InjectMocks
    private CreateUserValidator validator;

    private CreateUser validUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        validUser = new CreateUser();
        validUser.setName("John Doe");
        validUser.setEmail("john.doe@email.com");
        validUser.setCpf("111.444.777-35");
        validUser.setLogin("johndoe");
        validUser.setPassword("Password123!");
    }

    @Test
    void shouldValidateSuccessfullyWhenAllFieldsAreValid() {
        assertDoesNotThrow(() -> validator.validate(validUser));
    }

    @Test
    void shouldThrowExceptionWhenRequestIsNull() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> validator.validate(null));
        assertEquals("Request object cannot be null.", ex.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenRequiredFieldsAreBlank() {
        validUser.setName("");
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> validator.validate(validUser));
        assertEquals("All required fields must be filled.", ex.getMessage());
    }

    // Add more tests for other validation rules as needed (e.g., invalid email, password, login, etc.)
}
