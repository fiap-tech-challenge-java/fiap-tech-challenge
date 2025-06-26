package com.fiap.techchallenge.application.ports.in.user;

import com.fiap.techchallenge.application.ports.in.user.dtos.UserResponse;
import java.util.List;
import java.util.UUID;

public interface GetUserUseCase {
    UserResponse getById(UUID id);

    List<UserResponse> getAllUsers();
}
