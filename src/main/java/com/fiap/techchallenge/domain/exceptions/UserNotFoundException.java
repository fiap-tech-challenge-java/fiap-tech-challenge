package com.fiap.techchallenge.domain.exceptions;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException() {
        super("Usuário não encontrado.");
    }

    public UserNotFoundException(String message) {
        super(message);
    }
}
