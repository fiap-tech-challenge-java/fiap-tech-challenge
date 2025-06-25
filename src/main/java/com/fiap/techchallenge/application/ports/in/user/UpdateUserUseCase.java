package com.fiap.techchallenge.application.ports.in.user;

import com.fiap.techchallenge.application.ports.in.user.dtos.CreateUserCommand;
import com.fiap.techchallenge.application.ports.in.user.dtos.UserResponse;
import java.util.UUID;

public interface UpdateUserUseCase {
    UserResponse update(UUID id, CreateUserCommand command);
}