package com.fiap.techchallenge.domain.exceptions;

public class PasswordsDoNotMatchException extends IllegalArgumentException {
    public PasswordsDoNotMatchException() {
        super("As senhas não coincidem.");
    }

    public PasswordsDoNotMatchException(String message) {
        super(message);
    }
}
