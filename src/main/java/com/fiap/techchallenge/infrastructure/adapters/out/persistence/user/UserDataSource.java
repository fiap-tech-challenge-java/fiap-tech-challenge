package com.fiap.techchallenge.infrastructure.adapters.out.persistence.user;

import com.fiap.techchallenge.application.ports.in.user.dtos.*;
import com.fiap.techchallenge.application.ports.out.user.UserRepository;
import com.fiap.techchallenge.domain.exceptions.BusinessException;
import com.fiap.techchallenge.domain.exceptions.UserNotFoundException;
import com.fiap.techchallenge.domain.utils.UsernameValidator;
import com.fiap.techchallenge.infrastructure.adapters.out.persistence.user.entities.AddressUserEntity;
import com.fiap.techchallenge.infrastructure.adapters.out.persistence.user.entities.UserEntity;
import com.fiap.techchallenge.infrastructure.adapters.out.persistence.user.mapper.UserMapper;
import com.fiap.techchallenge.infrastructure.adapters.out.persistence.user.repositories.UserJpaRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserDataSource implements UserRepository {

    private final UserJpaRepository jpaRepository;
    private final PasswordEncoder passwordEncoder;
    private final UsernameValidator usernameValidator;

    private static final UserMapper USER_MAPPER = UserMapper.INSTANCE;

    public UserDataSource(UserJpaRepository jpaRepository, PasswordEncoder passwordEncoder,
            UsernameValidator usernameValidator) {
        this.jpaRepository = jpaRepository;
        this.passwordEncoder = passwordEncoder;
        this.usernameValidator = usernameValidator;
    }

    @Override
    public User create(CreateUser user) {
        UserEntity savedEntity = USER_MAPPER.mapToEntity(user);

        savedEntity.setActive(true);
        savedEntity.setPassword(passwordEncoder.encode(savedEntity.getPassword()));

        Address address = user.getAddress();
        if (address != null) {
            AddressUserEntity addressEntity = new AddressUserEntity(null, address.getPublicPlace(), address.getNumber(),
                    address.getComplement(), address.getNeighborhood(), address.getCity(), address.getState(),
                    address.getPostalCode(), savedEntity);

            savedEntity.setAddressesList(List.of(addressEntity));
        }

        jpaRepository.save(savedEntity);

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
        Optional<UserEntity> userEntity = jpaRepository.findByLogin(login);

        return userEntity.map(USER_MAPPER::mapToUser);
    }

    @Override
    public List<User> findAll() {
        return jpaRepository.findAll().stream().map(USER_MAPPER::mapToUser).toList();
    }

    @Override
    public User update(UUID id, UpdateUser updateUser) {
        UserEntity savedEntity = jpaRepository.findById(id).orElseThrow(UserNotFoundException::new);

        usernameValidator.validate(updateUser.getLogin());

        savedEntity.setName(updateUser.getName());
        savedEntity.setLogin(updateUser.getLogin());
        jpaRepository.save(savedEntity);

        return USER_MAPPER.mapToUser(savedEntity);
    }

    @Override
    public void deleteById(UUID id) {
        UserEntity savedEntity = jpaRepository.findById(id).orElseThrow(UserNotFoundException::new);

        savedEntity.setActive(false);
        jpaRepository.save(savedEntity);
    }

    @Override
    public void changePassword(ChangePassword changePassword) {
        UserEntity savedEntity = jpaRepository.findById(changePassword.getIdUser()).orElseThrow(UserNotFoundException::new);

        savedEntity.setPassword(changePassword.getNewPassword());
        jpaRepository.save(savedEntity);
    }

}
