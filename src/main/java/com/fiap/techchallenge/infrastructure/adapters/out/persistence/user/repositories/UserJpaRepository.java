package com.fiap.techchallenge.infrastructure.adapters.out.persistence.user.repositories;

import com.fiap.techchallenge.infrastructure.adapters.out.persistence.user.entities.UserEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserJpaRepository extends JpaRepository<UserEntity, UUID> {
    @EntityGraph(attributePaths = "addressesList")
    Optional<UserEntity> findByLogin(String login);

    Optional<UserEntity> findByEmailAndActiveTrue(String email);
}
