package com.fiap.techchallenge.infrastructure.validation;

import com.fiap.techchallenge.application.ports.in.user.dtos.CreateAddress;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CreateAddressValidatorTest {
    private CreateAddressValidator validator;
    private CreateAddress address;

    @BeforeEach
    void setUp() {
        validator = new CreateAddressValidator();
        address = new CreateAddress();
        address.setPublicPlace("Main St");
        address.setNumber("123");
        address.setCity("Springfield");
        address.setPostalCode("12345-678");
    }

    @Test
    void shouldValidateSuccessfullyWhenAllFieldsAreValid() {
        assertDoesNotThrow(() -> validator.validate(address));
    }

    @Test
    void shouldThrowExceptionWhenRequestIsNull() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> validator.validate(null));
        assertEquals("Address request object cannot be null.", ex.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenRequiredFieldsAreBlank() {
        address.setPublicPlace("");
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> validator.validate(address));
        assertEquals("Required address fields must be filled: street, number, city and postal code.", ex.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenPostalCodeIsInvalid() {
        address.setPostalCode("12345678");
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> validator.validate(address));
        assertEquals("Invalid postal code. Expected format: NNNNN-NNN", ex.getMessage());
    }
}

