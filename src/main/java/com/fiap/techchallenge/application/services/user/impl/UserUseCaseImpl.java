package com.fiap.techchallenge.application.services.user.impl;

import com.fiap.techchallenge.application.ports.in.user.dtos.*;
import com.fiap.techchallenge.application.ports.out.user.UserRepository;
import com.fiap.techchallenge.application.services.user.UserUseCase;
import com.fiap.techchallenge.infrastructure.validation.ChangePasswordValidator;
import com.fiap.techchallenge.infrastructure.validation.CreateUserValidator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserUseCaseImpl implements UserUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserUseCaseImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User create(CreateUser createUser) {
        CreateUserValidator.validate(createUser);

        createUser.setPassword(passwordEncoder.encode(createUser.getPassword()));
        return this.userRepository.create(createUser);
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public User getById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));
    }

    @Override
    public User update(UUID id, UpdateUser updateUser) {
        return this.userRepository.update(id, updateUser);
    }

    @Override
    public void delete(UUID id) {
        userRepository.deleteById(id);
    }

    @Override
    public void changePassword(ChangePassword changePassword) {
        ChangePasswordValidator.isValid(changePassword);

        ChangePassword changingPassword = new ChangePassword(changePassword.getIdUser(),
                passwordEncoder.encode(changePassword.getConfirmPassword()));

        this.userRepository.changePassword(changingPassword);
    }

}
