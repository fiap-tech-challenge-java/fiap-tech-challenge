package com.fiap.techchallenge.application.ports.in.user;

import java.util.UUID;

public interface DeleteUserUseCase {
    void delete(UUID id);
}
