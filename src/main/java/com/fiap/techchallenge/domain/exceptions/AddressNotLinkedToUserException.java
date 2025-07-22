package com.fiap.techchallenge.domain.exceptions;

public class AddressNotLinkedToUserException extends RuntimeException {
    public AddressNotLinkedToUserException() {
        super("Você não está vinculado a esse endereço.");
    }
}