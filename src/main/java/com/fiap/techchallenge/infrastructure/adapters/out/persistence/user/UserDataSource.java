package com.fiap.techchallenge.infrastructure.adapters.out.persistence.user;

import com.fiap.techchallenge.application.ports.in.user.dtos.CreateUser;
import com.fiap.techchallenge.application.ports.in.user.dtos.User;
import com.fiap.techchallenge.application.ports.out.user.UserRepository;
import com.fiap.techchallenge.domain.exceptions.BusinessException;
import com.fiap.techchallenge.infrastructure.adapters.out.persistence.user.repositories.UserJpaRepository;
import com.fiap.techchallenge.infrastructure.adapters.out.persistence.user.entities.UserEntity;
import com.fiap.techchallenge.infrastructure.adapters.out.persistence.user.mapper.UserMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserDataSource implements UserRepository {

    private final UserJpaRepository jpaRepository;

    private static final UserMapper USER_MAPPER = UserMapper.INSTANCE;

    public UserDataSource(UserJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public User create(CreateUser user) {
        UserEntity savedEntity = jpaRepository.save(USER_MAPPER.mapToEntity(user));

        return USER_MAPPER.mapToUser(savedEntity);
    }

    @Override
    public Optional<User> findById(UUID id) {
        UserEntity userEntity = jpaRepository.findById(id)
                .orElseThrow(() -> new BusinessException("User not found with id: " + id));
        return Optional.of(USER_MAPPER.mapToUser(userEntity));

    }

    @Override
    public Optional<User> findByLogin(String login) {
        return Optional.empty();
    }

    @Override
    public List<User> findAll() {
        return jpaRepository.findAll().stream().map(USER_MAPPER::mapToUser).toList();
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }

}
