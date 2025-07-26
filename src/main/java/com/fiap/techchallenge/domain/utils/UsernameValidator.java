package com.fiap.techchallenge.domain.utils;

import com.fiap.techchallenge.domain.exceptions.UsernameAlreadyExistsException;
import com.fiap.techchallenge.infrastructure.adapters.out.persistence.user.repositories.UserJpaRepository;
import org.springframework.stereotype.Component;

@Component
public class UsernameValidator {
    private final UserJpaRepository userJpaRepository;

    public UsernameValidator(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    public void validate(String username) {
        userJpaRepository.findByLogin(username).ifPresent(u -> {
            throw new UsernameAlreadyExistsException(username);
        });
    }

}
