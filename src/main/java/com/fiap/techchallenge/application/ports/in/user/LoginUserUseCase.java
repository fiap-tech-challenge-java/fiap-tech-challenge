package com.fiap.techchallenge.application.ports.in.user;

import com.fiap.techchallenge.application.ports.in.user.dtos.LoginCommand;
import com.fiap.techchallenge.application.ports.in.user.dtos.UserResponse;

public interface LoginUserUseCase {
    UserResponse execute(LoginCommand command);
}
