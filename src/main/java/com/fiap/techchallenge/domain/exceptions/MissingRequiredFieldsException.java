package com.fiap.techchallenge.domain.exceptions;

public class MissingRequiredFieldsException extends IllegalArgumentException {
    public MissingRequiredFieldsException() {
        super("Parâmetros obrigatórios ausentes.");
    }

    public MissingRequiredFieldsException(String message) {
        super(message);
    }
}
