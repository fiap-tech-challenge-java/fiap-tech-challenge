package com.fiap.techchallenge.application.ports.out.user;

import com.fiap.techchallenge.application.ports.in.user.dtos.Address;
import com.fiap.techchallenge.application.ports.in.user.dtos.CreateAddress;
import com.fiap.techchallenge.application.ports.in.user.dtos.UpdateAddress;

import java.util.List;
import java.util.UUID;

public interface AddressUserRepository {
    Address create(CreateAddress createAddress);

    List<Address> listAll(UUID userId);

    Address update(UpdateAddress updateAddress, UUID userId, UUID addressId);
}
