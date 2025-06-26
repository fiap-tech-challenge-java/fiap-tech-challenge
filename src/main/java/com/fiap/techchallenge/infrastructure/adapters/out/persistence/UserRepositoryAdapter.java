package com.fiap.techchallenge.infrastructure.adapters.out.persistence;

import com.fiap.techchallenge.application.ports.out.user.UserRepositoryPort;
import com.fiap.techchallenge.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepositoryPort {

    private final UserJpaRepository userJpaRepository;

    @Override
    public User save(User user) {
        UserJpaEntity entity = UserJpaEntity.fromDomainModel(user);
        UserJpaEntity savedEntity = userJpaRepository.save(entity);
        return savedEntity.toDomainModel();
    }

    @Override
    public Optional<User> findById(UUID id) {
        return userJpaRepository.findById(id).map(UserJpaEntity::toDomainModel);
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return userJpaRepository.findByLogin(login).map(UserJpaEntity::toDomainModel);
    }

    @Override
    public List<User> findAll() {
        return userJpaRepository.findAll().stream().map(UserJpaEntity::toDomainModel).collect(Collectors.toList());
    }

    @Override
    public void deleteById(UUID id) {
        userJpaRepository.deleteById(id);
    }
}
