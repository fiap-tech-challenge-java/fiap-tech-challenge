package com.fiap.techchallenge.domain.exceptions;

public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException(String email) {
        super("Account already created with the email: " + email);
    }
}