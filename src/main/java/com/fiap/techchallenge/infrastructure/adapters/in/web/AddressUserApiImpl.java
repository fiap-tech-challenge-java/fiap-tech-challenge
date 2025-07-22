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
        Address address = this.addressUserUseCase.create(ADDRESS_USER_MAPPER.mapToCreateAddress(createAddressRequest));

        return ResponseEntity.status(201).body(ADDRESS_USER_MAPPER.mapToAddressResponse(address));
    }

    @Override
    public ResponseEntity<List<AddressResponse>> listAddressesByUserId(UUID userId) {
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
        // Get authenticated user
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String authenticatedUsername = null;
        if (principal instanceof UserDetails) {
            authenticatedUsername = ((UserDetails) principal).getUsername();
        } else if (principal instanceof String) {
            authenticatedUsername = (String) principal;
        }
        // Check if userId matches authenticated user
        // You may need to fetch the user by username to get their UUID
        // For now, assume username == userId.toString() or add logic to fetch user UUID by username
        if (!userId.toString().equals(authenticatedUsername)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        UpdateAddress updateAddress = ADDRESS_USER_MAPPER.mapToUpdateAddress(updateAddressRequest);

        Address updated = addressUserUseCase.update(updateAddress, userId, addressId);

        AddressResponse body = ADDRESS_USER_MAPPER.mapToAddressResponse(updated);

        return ResponseEntity.ok(body);
    }

    @Override
    public ResponseEntity<Void> deleteAddressForUser(UUID userId, UUID addressId) {
        return null;
    }

}
