package com.fiap.techchallenge.infrastructure.adapters.in.web;

import com.fiap.techchallenge.application.ports.in.user.CreateUserUseCase;
import com.fiap.techchallenge.application.ports.in.user.DeleteUserUseCase;
import com.fiap.techchallenge.application.ports.in.user.GetUserUseCase;
import com.fiap.techchallenge.application.ports.in.user.LoginUserUseCase;
import com.fiap.techchallenge.application.ports.in.user.UpdateUserUseCase;
import com.fiap.techchallenge.application.ports.in.user.dtos.CreateUserCommand;
import com.fiap.techchallenge.application.ports.in.user.dtos.LoginCommand;
import com.fiap.techchallenge.application.ports.in.user.dtos.UserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final CreateUserUseCase createUserUseCase;
    private final GetUserUseCase getUserUseCase;
    private final UpdateUserUseCase updateUserUseCase;
    private final DeleteUserUseCase deleteUserUseCase;
    private final LoginUserUseCase loginUserUseCase;

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUserCommand command) {
        UserResponse response = createUserUseCase.execute(command);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable UUID id) {
        UserResponse response = getUserUseCase.getById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> response = getUserUseCase.getAllUsers();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable UUID id, @Valid @RequestBody CreateUserCommand command) {
        UserResponse response = updateUserUseCase.update(id, command);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        deleteUserUseCase.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@Valid @RequestBody LoginCommand command) {
        UserResponse response = loginUserUseCase.execute(command);
        return ResponseEntity.ok(response);
    }
}
