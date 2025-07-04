package com.fiap.techchallenge.infrastructure.adapters.in.web;

import com.fiap.techchallenge.application.ports.in.user.dtos.User;
import com.fiap.techchallenge.application.services.user.UserUseCase;
import com.fiap.techchallenge.application.ports.in.user.dtos.CreateUser;
import com.fiap.techchallenge.infrastructure.adapters.in.mapper.UserApiMapper;
import com.fiap.techchallenge.api.UsersApi;
import com.fiap.techchallenge.application.ports.in.user.dtos.ChangePassword;
import com.fiap.techchallenge.model.*;
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
    public ResponseEntity<CreateUserResponse> createUser(CreateUserRequest createUserRequest) {
        CreateUser createMapper = USERS_API_MAPPER.mapToCreateUser(createUserRequest);

        CreateUserResponse userResponse = USERS_API_MAPPER
                .mapToCreateUserResponse(this.userUseCase.create(createMapper));

        return ResponseEntity.status(201).body(userResponse);
    }

    @Override
    public ResponseEntity<UserResponse> updateUser(UUID id, UpdateUserRequest updateUserRequest) {
        User userResponse = this.userUseCase.update(id, USERS_API_MAPPER.mapToUpdateUser(updateUserRequest));

        return ResponseEntity.ok(USERS_API_MAPPER.mapToUserResponse(userResponse));
    }

    @Override
    public ResponseEntity<UserResponse> getUser(UUID id) {
        User user = this.userUseCase.getById(id);
        UserResponse response = USERS_API_MAPPER.mapToUserResponse(user);

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<List<UserResponse>> getUsers() {
        List<User> users = this.userUseCase.getAll();
        List<UserResponse> responses = users.stream().map(USERS_API_MAPPER::mapToUserResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    @Override
    public ResponseEntity<Void> deleteUser(UUID id) {
        this.userUseCase.delete(id);

        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> changePassword(ChangePasswordRequest changePasswordRequest) {
        ChangePassword changePassword = USERS_API_MAPPER.mapToChangePassword(changePasswordRequest);

        this.userUseCase.changePassword(changePassword);

        return ResponseEntity.noContent().build();
    }
}
