package com.fiap.techchallenge.infrastructure.adapters.out.persistence.user;

import com.fiap.techchallenge.application.ports.in.user.dtos.Address;
import com.fiap.techchallenge.application.ports.in.user.dtos.CreateAddress;
import com.fiap.techchallenge.infrastructure.adapters.out.persistence.user.entities.AddressUserEntity;
import com.fiap.techchallenge.infrastructure.adapters.out.persistence.user.entities.UserEntity;
import com.fiap.techchallenge.infrastructure.adapters.out.persistence.user.mapper.AddressUserMapper;
import com.fiap.techchallenge.infrastructure.adapters.out.persistence.user.repositories.AddressUserJpaRepository;
import com.fiap.techchallenge.infrastructure.adapters.out.persistence.user.repositories.UserJpaRepository;
import com.fiap.techchallenge.domain.exceptions.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddressUserDataSourceTest {
    @Mock
    private AddressUserJpaRepository addressUserJpaRepository;
    @Mock
    private UserJpaRepository userJpaRepository;

    @InjectMocks
    private AddressUserDataSource addressUserDataSource;

    private CreateAddress createAddress;
    private UserEntity userEntity;
    private AddressUserEntity addressUserEntity;
    private Address address;
    private UUID userId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        createAddress = new CreateAddress();
        createAddress.setIdUser(userId);
        userEntity = new UserEntity();
        addressUserEntity = new AddressUserEntity();
        address = new Address();
        // ...set other fields as needed
    }

    @Test
    void shouldThrowUserNotFoundExceptionWhenUserDoesNotExist() {
        // Arrange
        when(userJpaRepository.findById(userId)).thenReturn(Optional.empty());
        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> addressUserDataSource.create(createAddress));
        verify(userJpaRepository).findById(userId);
        verify(addressUserJpaRepository, never()).save(any());
    }
}
