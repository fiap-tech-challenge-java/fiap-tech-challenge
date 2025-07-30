package com.fiap.techchallenge.application.services.user;

import com.fiap.techchallenge.application.ports.in.user.dtos.Address;
import com.fiap.techchallenge.application.ports.in.user.dtos.CreateAddress;
import com.fiap.techchallenge.application.ports.in.user.dtos.UpdateAddress;

import java.util.List;
import java.util.UUID;

public interface AddressUserUseCase {
    Address create(CreateAddress createAddress);

    List<Address> listAll(UUID idUser);

    Address update(UpdateAddress updateAddress, UUID userId, UUID addressId);

    void delete(UUID idUser, UUID idAddress);
}
