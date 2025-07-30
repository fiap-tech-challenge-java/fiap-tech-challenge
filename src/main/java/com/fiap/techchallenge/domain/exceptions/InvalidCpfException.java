package com.fiap.techchallenge.domain.exceptions;

public class InvalidCpfException extends IllegalArgumentException {

    public InvalidCpfException() {
        super("Invalid CPF.");
    }

    public InvalidCpfException(String message) {
        super(message);
    }
}
