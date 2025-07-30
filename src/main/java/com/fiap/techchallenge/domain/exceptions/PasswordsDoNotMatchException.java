package com.fiap.techchallenge.domain.exceptions;

public class PasswordsDoNotMatchException extends IllegalArgumentException {
    public PasswordsDoNotMatchException() {
        super("Passwords do not match.");
    }

    public PasswordsDoNotMatchException(String message) {
        super(message);
    }
}
