package com.fiap.techchallenge.infrastructure.adapters.in.web;

import com.fiap.techchallenge.application.services.user.UserUseCase;
import com.fiap.techchallenge.application.ports.in.user.dtos.CreateUser;
import com.fiap.techchallenge.infrastructure.adapters.in.mapper.UserApiMapper;
import com.fiap.techchallenge.user.api.UsersApi;
import com.fiap.techchallenge.user.model.UserRequest;
import com.fiap.techchallenge.user.model.UserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
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
        return null;
    }

    @Override
    public ResponseEntity<UserResponse> getUser(UUID id) {
        return null;
    }

    @Override
    public ResponseEntity<List<UserResponse>> getUsers() {
        return null;
    }

    @Override
    public ResponseEntity<UserResponse> updateUser(UUID id, UserRequest userRequest) {
        return null;
    }
}
