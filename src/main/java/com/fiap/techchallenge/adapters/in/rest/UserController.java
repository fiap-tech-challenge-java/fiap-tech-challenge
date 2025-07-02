package com.fiap.techchallenge.adapters.in.rest;

import com.fiap.techchallenge.api.UsersApi;
import com.fiap.techchallenge.model.UserRequest;
import com.fiap.techchallenge.model.UserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
public class UserController implements UsersApi {
    @Override
    public ResponseEntity<UserResponse> createUser(UserRequest userRequest) {
        return null;
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
