package com.fiap.techchallenge.infrastructure.adapters.out.persistence.user.repositories;

import com.fiap.techchallenge.infrastructure.adapters.out.persistence.user.entities.AddressUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AddressUserJpaRepository extends JpaRepository<AddressUserEntity, UUID> {
    List<AddressUserEntity> findAllByUserId(UUID userId);

    Optional<AddressUserEntity> findByIdAndUserId(UUID idAddress, UUID idUser);
}
