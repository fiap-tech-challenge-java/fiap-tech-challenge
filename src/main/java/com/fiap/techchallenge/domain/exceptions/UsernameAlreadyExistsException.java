package com.fiap.techchallenge.domain.exceptions;

public class UsernameAlreadyExistsException extends RuntimeException {
    public UsernameAlreadyExistsException(String login) {
        super("Login já está em uso: " + login);
    }
}