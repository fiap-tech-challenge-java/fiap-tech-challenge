package com.fiap.techchallenge.application.services.user;

import com.fiap.techchallenge.application.ports.in.user.CreateUserUseCase;
import com.fiap.techchallenge.application.ports.in.user.DeleteUserUseCase;
import com.fiap.techchallenge.application.ports.in.user.GetUserUseCase;
import com.fiap.techchallenge.application.ports.in.user.LoginUserUseCase;
import com.fiap.techchallenge.application.ports.in.user.UpdateUserUseCase;
import com.fiap.techchallenge.application.ports.in.user.dtos.CreateUserCommand;
import com.fiap.techchallenge.application.ports.in.user.dtos.LoginCommand;
import com.fiap.techchallenge.application.ports.in.user.dtos.UserResponse;
import com.fiap.techchallenge.application.ports.out.user.UserRepositoryPort;
import com.fiap.techchallenge.domain.exceptions.InvalidLoginException;
import com.fiap.techchallenge.domain.exceptions.UserNotFoundException;
import com.fiap.techchallenge.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserApplicationService
        implements CreateUserUseCase, GetUserUseCase, UpdateUserUseCase, DeleteUserUseCase, LoginUserUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponse execute(CreateUserCommand command) {
        String hashedPassword = passwordEncoder.encode(command.password());
        User user = new User(command.name(), command.email(), command.login(), hashedPassword, command.address(),
                command.type());
        User savedUser = userRepositoryPort.save(user);
        return mapToUserResponse(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getById(UUID id) {
        User user = userRepositoryPort.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + id + " not found."));
        return mapToUserResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {
        return userRepositoryPort.findAll().stream().map(this::mapToUserResponse).collect(Collectors.toList());
    }

    @Override
    public UserResponse update(UUID id, CreateUserCommand command) {
        User existingUser = userRepositoryPort.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + id + " not found."));

        existingUser.setName(command.name());
        existingUser.setEmail(command.email());
        existingUser.setLogin(command.login());
        existingUser.setAddress(command.address());
        existingUser.setType(command.type());
        existingUser.setLastModifiedDate(LocalDateTime.now());

        if (command.password() != null && !command.password().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(command.password()));
        }

        User updatedUser = userRepositoryPort.save(existingUser);
        return mapToUserResponse(updatedUser);
    }

    @Override
    public void delete(UUID id) {
        userRepositoryPort.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + id + " not found."));
        userRepositoryPort.deleteById(id);
    }

    @Override
    public UserResponse execute(LoginCommand command) {
        User user = userRepositoryPort.findByLogin(command.login())
                .orElseThrow(() -> new InvalidLoginException("Invalid credentials."));

        if (!passwordEncoder.matches(command.password(), user.getPassword())) {
            throw new InvalidLoginException("Invalid credentials.");
        }

        return mapToUserResponse(user);
    }

    private UserResponse mapToUserResponse(User user) {
        return new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getLogin(), user.getAddress(),
                user.getType(), user.getLastModifiedDate());
    }
}
