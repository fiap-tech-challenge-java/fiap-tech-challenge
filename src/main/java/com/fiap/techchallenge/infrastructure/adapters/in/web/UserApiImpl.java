package com.fiap.techchallenge.infrastructure.adapters.in.web;

import com.fiap.techchallenge.application.ports.in.user.dtos.User;
import com.fiap.techchallenge.application.services.user.UserUseCase;
import com.fiap.techchallenge.application.ports.in.user.dtos.CreateUser;
import com.fiap.techchallenge.infrastructure.adapters.in.mapper.UserApiMapper;
import com.fiap.techchallenge.api.UsersApi;
import com.fiap.techchallenge.model.ChangePassword;
import com.fiap.techchallenge.model.UserRequest;
import com.fiap.techchallenge.model.UserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
public class UserApiImpl implements UsersApi {

    private final UserUseCase userUseCase;

    private static final UserApiMapper USERS_API_MAPPER = UserApiMapper.INSTANCE;

    public UserApiImpl(UserUseCase userUseCase) {
        this.userUseCase = userUseCase;
    }

    @Override
    public ResponseEntity<UserResponse> createUser(UserRequest userRequest) {
        CreateUser createMapper = USERS_API_MAPPER.mapToCreateUser(userRequest);

        UserResponse userResponse = USERS_API_MAPPER.mapToUserResponse(this.userUseCase.create(createMapper));

        return ResponseEntity.status(201).body(userResponse);
    }

    @Override
    public ResponseEntity<Void> deleteUser(UUID id) {
        userUseCase.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<UserResponse> getUser(UUID id) {
        User user = userUseCase.getById(id);
        UserResponse response = USERS_API_MAPPER.mapToUserResponse(user);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<List<UserResponse>> getUsers() {

        List<User> users = userUseCase.getAll();
        List<UserResponse> responses = users.stream().map(USERS_API_MAPPER::mapToUserResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    @Override
    public ResponseEntity<UserResponse> updateUser(UUID id, UserRequest userRequest) {
        CreateUser updateMapper = USERS_API_MAPPER.mapToCreateUser(userRequest);
        User updatedUser = userUseCase.update(id, updateMapper);
        UserResponse response = USERS_API_MAPPER.mapToUserResponse(updatedUser);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Void> changePassword(ChangePassword changePassword) {
        return null;
    }
}
