package com.fiap.techchallenge.domain.exceptions;

public class AddressNotFoundException extends RuntimeException {

    public AddressNotFoundException() {
        super("Endereço não encontrado para o usuário informado.");
    }

    public AddressNotFoundException(String message) {
        super(message);
    }
}
