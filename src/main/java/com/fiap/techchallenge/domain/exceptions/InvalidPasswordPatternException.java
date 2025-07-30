package com.fiap.techchallenge.domain.exceptions;

public class InvalidPasswordPatternException extends IllegalArgumentException {
    public InvalidPasswordPatternException() {
        super("The password must be at least 5 characters long, containing at least 1 uppercase letter and 1 number.");
    }

    public InvalidPasswordPatternException(String message) {
        super(message);
    }
}
