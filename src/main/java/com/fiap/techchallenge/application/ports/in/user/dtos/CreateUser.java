package com.fiap.techchallenge.application.ports.in.user.dtos;

import com.fiap.techchallenge.domain.model.enums.RoleEnum;

public record CreateUser(String name, String email, String login, String password, Address address, RoleEnum roleEnum) {
}