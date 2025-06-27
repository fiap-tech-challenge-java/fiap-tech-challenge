package com.fiap.techchallenge.application.ports.in.user.dtos;

import jakarta.validation.constraints.NotBlank;

public record Login(@NotBlank(message = "Login cannot be blank") String login,

        @NotBlank(message = "Password cannot be blank") String password) {
}
