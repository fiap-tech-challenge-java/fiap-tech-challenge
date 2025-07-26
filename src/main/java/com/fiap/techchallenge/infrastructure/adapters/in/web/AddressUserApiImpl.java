package com.fiap.techchallenge.infrastructure.adapters.in.web;

import com.fiap.techchallenge.api.AddressesApi;
import com.fiap.techchallenge.application.ports.in.user.dtos.Address;
import com.fiap.techchallenge.application.ports.in.user.dtos.UpdateAddress;
import com.fiap.techchallenge.application.services.user.AddressUserUseCase;
import com.fiap.techchallenge.infrastructure.adapters.in.mapper.AddressUserApiMapper;
import com.fiap.techchallenge.model.AddressResponse;
import com.fiap.techchallenge.model.CreateAddressRequest;
import com.fiap.techchallenge.model.UpdateAddressRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.UUID;

@RestController
public class AddressUserApiImpl implements AddressesApi {
    private final AddressUserUseCase addressUserUseCase;

    private static final AddressUserApiMapper ADDRESS_USER_MAPPER = AddressUserApiMapper.INSTANCE;

    public AddressUserApiImpl(AddressUserUseCase addressUserUseCase) {
        this.addressUserUseCase = addressUserUseCase;
    }

    @Override
    public ResponseEntity<AddressResponse> createAddressForUser(UUID userId,
            CreateAddressRequest createAddressRequest) {
        if (!isAuthenticatedUser(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        var createAddress = ADDRESS_USER_MAPPER.mapToCreateAddress(createAddressRequest);
        createAddress.setIdUser(userId);
        Address address = this.addressUserUseCase.create(createAddress);

        return ResponseEntity.status(201).body(ADDRESS_USER_MAPPER.mapToAddressResponse(address));
    }

    @Override
    public ResponseEntity<List<AddressResponse>> listAddressesByUserId(UUID userId) {
        if (!isAuthenticatedUser(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        List<Address> addresses = addressUserUseCase.listAll(userId);

        if (addresses.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<AddressResponse> response = ADDRESS_USER_MAPPER.mapToAddressResponseList(addresses);

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<AddressResponse> updateAddressForUser(UUID userId, UUID addressId,
            UpdateAddressRequest updateAddressRequest) {
        if (!isAuthenticatedUser(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        UpdateAddress updateAddress = ADDRESS_USER_MAPPER.mapToUpdateAddress(updateAddressRequest);

        Address updated = addressUserUseCase.update(updateAddress, userId, addressId);

        AddressResponse body = ADDRESS_USER_MAPPER.mapToAddressResponse(updated);

        return ResponseEntity.ok(body);
    }

    @Override
    public ResponseEntity<Void> deleteAddressForUser(UUID userId, UUID addressId) {
        if (!isAuthenticatedUser(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        addressUserUseCase.delete(userId, addressId);
        return ResponseEntity.noContent().build();
    }

    // private functions
    private boolean isAuthenticatedUser(UUID userId) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof com.fiap.techchallenge.infrastructure.config.UserDetailsImpl) {
            UUID authenticatedId = ((com.fiap.techchallenge.infrastructure.config.UserDetailsImpl) principal).getId();
            return userId.equals(authenticatedId);
        }
        return false;
    }

}
