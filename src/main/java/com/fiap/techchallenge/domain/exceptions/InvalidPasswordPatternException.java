package com.fiap.techchallenge.domain.exceptions;

public class InvalidPasswordPatternException extends IllegalArgumentException {
    public InvalidPasswordPatternException() {
        super("A senha precisa ter mínimo de 5 caracteres, contendo ao menos 1 letra maiúscula e 1 número.");
    }

    public InvalidPasswordPatternException(String message) {
        super(message);
    }
}
