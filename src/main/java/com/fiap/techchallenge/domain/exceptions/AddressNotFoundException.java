package com.fiap.techchallenge.domain.exceptions;

public class AddressNotFoundException extends RuntimeException {

    public AddressNotFoundException() {
        super("Address not found for the specified user.");
    }

    public AddressNotFoundException(String message) {
        super(message);
    }
}
