package com.fiap.techchallenge.application.ports.in.user.dtos;

import com.fiap.techchallenge.domain.model.User.UserType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateUserCommand(
        @NotBlank(message = "Name cannot be blank")
        String name,
        @NotBlank(message = "Email cannot be blank")
        @Email(message = "Email should be valid")
        String email,
        @NotBlank(message = "Login cannot be blank")
        String login,
        @NotBlank(message = "Password cannot be blank")
        @Size(min = 6, message = "Password must be at least 6 characters long")
        String password,
        String address,
        @NotNull(message = "User type cannot be null")
        UserType type
) {}