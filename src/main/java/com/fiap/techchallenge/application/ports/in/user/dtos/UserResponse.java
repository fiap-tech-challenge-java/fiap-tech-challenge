package com.fiap.techchallenge.application.ports.in.user.dtos;

import com.fiap.techchallenge.domain.model.User.UserType;
import java.time.LocalDateTime;
import java.util.UUID;

public record UserResponse(UUID id, String name, String email, String login, String address, UserType type,
        LocalDateTime lastModifiedDate) {
}
