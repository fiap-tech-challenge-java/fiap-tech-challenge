package com.fiap.techchallenge.domain.exceptions;

public class InvalidEmailPatternException extends IllegalArgumentException {
    public InvalidEmailPatternException() {
        super("Invalid email format.");
    }

    public InvalidEmailPatternException(String message) {
        super(message);
    }
}
