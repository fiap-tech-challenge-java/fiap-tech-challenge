package com.fiap.techchallenge.application.services.user;

import com.fiap.techchallenge.application.ports.in.user.dtos.CreateUser;
import com.fiap.techchallenge.application.ports.in.user.dtos.Login;
import com.fiap.techchallenge.application.ports.in.user.dtos.User;

import java.util.List;
import java.util.UUID;

public interface UserUseCase {
    User login(Login login);

    User create(CreateUser createUser);

    List<User> getAll();

    User getById(UUID id);

    User update(UUID id, CreateUser command);

    void delete(UUID id);
}
