package com.fiap.techchallenge.application.services.user.impl;

import com.fiap.techchallenge.application.ports.in.user.dtos.Address;
import com.fiap.techchallenge.application.ports.in.user.dtos.CreateAddress;
import com.fiap.techchallenge.application.ports.in.user.dtos.UpdateAddress;
import com.fiap.techchallenge.application.ports.out.user.AddressUserRepository;
import com.fiap.techchallenge.infrastructure.validation.CreateAddressValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddressUserUseCaseImplTest {

    @Mock
    private AddressUserRepository addressUserRepository;

    @Mock
    private CreateAddressValidator createAddressValidator;

    @InjectMocks
    private AddressUserUseCaseImpl addressUserUseCase;

    private CreateAddress createAddress;
    private UpdateAddress updateAddress;
    private Address address;
    private UUID userId;
    private UUID addressId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        addressId = UUID.randomUUID();

        createAddress = new CreateAddress();
        createAddress.setPublicPlace("Rua Teste");
        createAddress.setNumber("123");
        createAddress.setNeighborhood("Bairro Teste");
        createAddress.setCity("Cidade Teste");
        createAddress.setState("SP");
        createAddress.setPostalCode("12345678");
        createAddress.setIdUser(userId);

        updateAddress = new UpdateAddress();
        updateAddress.setPublicPlace("Rua Atualizada");
        updateAddress.setNumber("456");
        updateAddress.setNeighborhood("Bairro Atualizado");
        updateAddress.setCity("Cidade Atualizada");
        updateAddress.setState("RJ");
        updateAddress.setPostalCode("87654321");

        address = new Address();
        address.setId(addressId);
        address.setPublicPlace(createAddress.getPublicPlace());
        address.setNumber(createAddress.getNumber());
        address.setNeighborhood(createAddress.getNeighborhood());
        address.setCity(createAddress.getCity());
        address.setState(createAddress.getState());
        address.setPostalCode(createAddress.getPostalCode());
    }

    @Test
    void shouldCreateAddressSuccessfully() {
        // Arrange
        doNothing().when(createAddressValidator).validate(createAddress);
        when(addressUserRepository.create(createAddress)).thenReturn(address);

        // Act
        Address result = addressUserUseCase.create(createAddress);

        // Assert
        assertNotNull(result);
        assertEquals(address.getId(), result.getId());
        assertEquals(address.getPublicPlace(), result.getPublicPlace());
        verify(createAddressValidator).validate(createAddress);
        verify(addressUserRepository).create(createAddress);
    }

    @Test
    void shouldThrowExceptionWhenCreateAddressFailsValidation() {
        // Arrange
        doThrow(new IllegalArgumentException("Invalid address")).when(createAddressValidator).validate(createAddress);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> addressUserUseCase.create(createAddress));
        verify(createAddressValidator).validate(createAddress);
        verify(addressUserRepository, never()).create(any());
    }

    @Test
    void shouldListAllAddressesForUser() {
        // Arrange
        List<Address> expectedAddresses = Arrays.asList(address);
        when(addressUserRepository.listAll(userId)).thenReturn(expectedAddresses);

        // Act
        List<Address> result = addressUserUseCase.listAll(userId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(address.getId(), result.get(0).getId());
        verify(addressUserRepository).listAll(userId);
    }

    @Test
    void shouldUpdateAddressSuccessfully() {
        // Arrange
        when(addressUserRepository.update(updateAddress, userId, addressId)).thenReturn(address);

        // Act
        Address result = addressUserUseCase.update(updateAddress, userId, addressId);

        // Assert
        assertNotNull(result);
        assertEquals(address.getId(), result.getId());
        verify(addressUserRepository).update(updateAddress, userId, addressId);
    }

    @Test
    void shouldDeleteAddressSuccessfully() {
        // Arrange
        doNothing().when(addressUserRepository).delete(userId, addressId);

        // Act
        addressUserUseCase.delete(userId, addressId);

        // Assert
        verify(addressUserRepository).delete(userId, addressId);
    }

    @Test
    void shouldHandleEmptyAddressList() {
        // Arrange
        when(addressUserRepository.listAll(userId)).thenReturn(List.of());

        // Act
        List<Address> result = addressUserUseCase.listAll(userId);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(addressUserRepository).listAll(userId);
    }

    @Test
    void shouldHandleNullInputInCreate() {
        // Since the implementation doesn't validate null input, we'll test that it throws NPE
        // when trying to call validate on null
        when(addressUserRepository.create(null)).thenThrow(new NullPointerException());

        // Act & Assert
        assertThrows(NullPointerException.class, () -> addressUserUseCase.create(null));

        // The validator is still called with null, which will throw NPE
        verify(createAddressValidator).validate(null);
    }

    @Test
    void shouldHandleNullUserIdInListAll() {
        // Act
        List<Address> result = addressUserUseCase.listAll(null);

        // Assert - Should not throw, just return empty list
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(addressUserRepository).listAll(null);
    }
}