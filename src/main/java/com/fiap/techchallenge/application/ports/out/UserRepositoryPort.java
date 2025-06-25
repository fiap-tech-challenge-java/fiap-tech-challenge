package com.fiap.techchallenge.application.ports.out;

import com.fiap.techchallenge.domain.model.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepositoryPort {
    User save(User user);
    Optional<User> findById(UUID id);
    Optional<User> findByLogin(String login);
    List<User> findAll();
    void deleteById(UUID id);
}
