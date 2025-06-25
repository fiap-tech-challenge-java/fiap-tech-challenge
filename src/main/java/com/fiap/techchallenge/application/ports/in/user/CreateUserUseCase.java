package com.fiap.techchallenge.application.ports.in.user;

import com.fiap.techchallenge.application.ports.in.user.dtos.CreateUserCommand;
import com.fiap.techchallenge.application.ports.in.user.dtos.UserResponse;

public interface CreateUserUseCase {
    UserResponse execute(CreateUserCommand command);
}