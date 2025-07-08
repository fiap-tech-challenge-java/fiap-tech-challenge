package com.fiap.techchallenge.domain.exceptions;

public class InvalidCpfException extends IllegalArgumentException {

    public InvalidCpfException() {
        super("CPF inválido.");
    }

    public InvalidCpfException(String message) {
        super(message);
    }
}
