package com.fiap.techchallenge.application.services.user;

import com.fiap.techchallenge.application.ports.in.user.dtos.*;
import com.fiap.techchallenge.model.UpdateUserRequest;

import java.util.List;
import java.util.UUID;

public interface UserUseCase {
    User login(Login login);

    User create(CreateUser createUser);

    List<User> getAll();

    User getById(UUID id);

    User update(UUID id, UpdateUser updateUser);

    void delete(UUID id);

    void changePassword(ChangePassword changePassword);
}
