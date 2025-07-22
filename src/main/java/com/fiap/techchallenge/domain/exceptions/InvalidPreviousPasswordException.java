package com.fiap.techchallenge.domain.exceptions;

public class InvalidPreviousPasswordException extends RuntimeException {
    public InvalidPreviousPasswordException() {
        super("Senha anterior inválida.");
    }
}