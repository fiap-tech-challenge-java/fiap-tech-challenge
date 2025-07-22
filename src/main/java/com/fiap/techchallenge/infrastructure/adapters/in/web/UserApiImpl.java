package com.fiap.techchallenge.infrastructure.adapters.in.web;

import com.fiap.techchallenge.application.ports.in.user.dtos.User;
import com.fiap.techchallenge.application.services.user.UserUseCase;
import com.fiap.techchallenge.application.ports.in.user.dtos.CreateUser;
import com.fiap.techchallenge.domain.model.enums.RoleEnum;
import com.fiap.techchallenge.infrastructure.adapters.in.mapper.UserApiMapper;
import com.fiap.techchallenge.api.UsersApi;
import com.fiap.techchallenge.application.ports.in.user.dtos.ChangePassword;
import com.fiap.techchallenge.infrastructure.config.UserDetailsImpl;
import com.fiap.techchallenge.model.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.http.HttpStatus;

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

        User user = userUseCase.create(createMapper);
        CreateUserResponse userResponse = USERS_API_MAPPER.mapToCreateUserResponse(user);

        return ResponseEntity.status(201).body(userResponse);
    }

    @Override
    public ResponseEntity<UserResponse> updateUser(UUID id, UpdateUserRequest updateUserRequest) {
        if (isAuthenticatedUser(id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        User userResponse = this.userUseCase.update(id, USERS_API_MAPPER.mapToUpdateUser(updateUserRequest));
        return ResponseEntity.ok(USERS_API_MAPPER.mapToUserResponse(userResponse));
    }

    @Override
    public ResponseEntity<UserResponse> getUser(UUID id) {
        if (isAuthenticatedUser(id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        User user = this.userUseCase.getById(id);
        UserResponse response = USERS_API_MAPPER.mapToUserResponse(user);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<List<UserResponse>> getUsers() {
        String authenticatedUsername = getAuthenticatedUsername();
        // Fetch authenticated user object
        User authenticatedUser = null;
        for (User u : userUseCase.getAll()) {
            if (u.getLogin().equals(authenticatedUsername) || u.getId().toString().equals(authenticatedUsername)) {
                authenticatedUser = u;
                break;
            }
        }
        if (authenticatedUser == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        // OWNER can see themselves and all CUSTOMERS
        if (authenticatedUser.getRole() == RoleEnum.OWNER) {
            final User finalAuthenticatedUser = authenticatedUser;
            List<UserResponse> responses = userUseCase.getAll().stream()
                .filter(u -> u.getRole() == RoleEnum.CUSTOMER || u.getId().equals(finalAuthenticatedUser.getId()))
                .map(USERS_API_MAPPER::mapToUserResponse)
                .collect(Collectors.toList());
            return ResponseEntity.ok(responses);
        }
        // CUSTOMER can only see themselves
        if (authenticatedUser.getRole() == RoleEnum.CUSTOMER) {
            final User finalAuthenticatedUser = authenticatedUser;
            List<UserResponse> responses = userUseCase.getAll().stream()
                .filter(u -> u.getId().equals(finalAuthenticatedUser.getId()))
                .map(USERS_API_MAPPER::mapToUserResponse)
                .collect(Collectors.toList());
            if (responses.size() == 1) {
                return ResponseEntity.ok(responses);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .header("X-Restriction-Reason", "CUSTOMER users cannot view other CUSTOMERS or OWNERS.")
                    .body(null);
            }
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
    }

    @Override
    public ResponseEntity<Void> deleteUser(UUID id) {
        if (isAuthenticatedUser(id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        this.userUseCase.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> changePassword(ChangePasswordRequest changePasswordRequest) {
        ChangePassword changePassword = USERS_API_MAPPER.mapToChangePassword(changePasswordRequest);

        this.userUseCase.changePassword(changePassword);

        return ResponseEntity.noContent().build();
    }

    // private functions
    private String getAuthenticatedUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else if (principal instanceof String) {
            return (String) principal;
        }
        return null;
    }

    private boolean isAuthenticatedUser(UUID id) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetailsImpl) {
            UUID authenticatedId = ((UserDetailsImpl) principal).getId();
            return !id.equals(authenticatedId);
        }
        return true;
    }
}
