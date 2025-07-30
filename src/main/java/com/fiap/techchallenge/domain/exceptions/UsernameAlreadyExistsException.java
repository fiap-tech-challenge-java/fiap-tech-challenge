package com.fiap.techchallenge.domain.exceptions;

public class UsernameAlreadyExistsException extends RuntimeException {
    public UsernameAlreadyExistsException(String login) {
        super("Login is already in use:" + login);
    }
}