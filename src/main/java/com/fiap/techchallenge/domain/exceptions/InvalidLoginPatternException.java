package com.fiap.techchallenge.domain.exceptions;

public class InvalidLoginPatternException extends RuntimeException {

    public InvalidLoginPatternException() {
        super("O login deve ter entre 5 e 20 caracteres.");
    }

    public InvalidLoginPatternException(String message) {
        super(message);
    }
}
