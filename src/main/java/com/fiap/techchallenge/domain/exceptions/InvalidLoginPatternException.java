package com.fiap.techchallenge.domain.exceptions;

public class InvalidLoginPatternException extends RuntimeException {

    public InvalidLoginPatternException() {
        super("Login must be between 5 and 20 characters.");
    }

    public InvalidLoginPatternException(String message) {
        super(message);
    }
}
