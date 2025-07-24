package com.fiap.techchallenge.domain.exceptions;

public class MissingRequiredFieldsException extends IllegalArgumentException {
    public MissingRequiredFieldsException() {
        super("Required parameters missing.");
    }

    public MissingRequiredFieldsException(String message) {
        super(message);
    }
}
