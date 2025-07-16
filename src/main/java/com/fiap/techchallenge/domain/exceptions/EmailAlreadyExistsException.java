package com.fiap.techchallenge.domain.exceptions;

public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException(String email) {
        super("Conta já criada com o e‑mail: " + email);
    }
}