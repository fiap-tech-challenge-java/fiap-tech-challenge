package com.fiap.techchallenge.application.services.user.impl;

import com.fiap.techchallenge.application.ports.in.user.dtos.ChangePassword;
import com.fiap.techchallenge.application.ports.in.user.dtos.CreateUser;
import com.fiap.techchallenge.application.ports.in.user.dtos.Login;
import com.fiap.techchallenge.application.ports.in.user.dtos.User;
import com.fiap.techchallenge.application.ports.out.user.UserRepository;
import com.fiap.techchallenge.application.services.user.UserUseCase;
import com.fiap.techchallenge.infrastructure.validation.ChangePasswordValidator;
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
    public User login(Login login) {
        return null;
    }

    @Override
    public User create(CreateUser createUser) {
        return this.userRepository.create(createUser);
    }

    @Override
    public List<User> getAll() {
        return List.of();
    }

    @Override
    public User getById(UUID id) {
        return null;
    }

    @Override
    public User update(UUID id, CreateUser createUser) {
        return null;
    }

    @Override
    public void delete(UUID id) {

    }

    @Override
    public void changePassword(ChangePassword changePassword) {
        ChangePasswordValidator.isValid(changePassword);

        ChangePassword changingPassword = new ChangePassword(changePassword.getIdUser(),
                passwordEncoder.encode(changePassword.getConfirmPassword()));

        this.userRepository.changePassword(changingPassword);
    }

}
