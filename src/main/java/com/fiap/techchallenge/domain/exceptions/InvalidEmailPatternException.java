package com.fiap.techchallenge.domain.exceptions;

public class InvalidEmailPatternException extends IllegalArgumentException {
    public InvalidEmailPatternException() {
        super("E-mail em formato inválido.");
    }

    public InvalidEmailPatternException(String message) {
        super(message);
    }
}
