package com.fiap.techchallenge.domain.exceptions;

public class InvalidPasswordPatternException extends IllegalArgumentException {
    public InvalidPasswordPatternException() {
        super("A senha deve conter pelo menos uma letra maiúscula, um número e no mínimo 5 caracteres.");
    }

    public InvalidPasswordPatternException(String message) {
        super(message);
    }
}
