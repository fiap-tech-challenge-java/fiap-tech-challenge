package com.fiap.techchallenge.application.services.user.impl;

import com.fiap.techchallenge.application.ports.in.user.dtos.Address;
import com.fiap.techchallenge.application.ports.in.user.dtos.CreateAddress;
import com.fiap.techchallenge.application.ports.in.user.dtos.UpdateAddress;
import com.fiap.techchallenge.application.ports.out.user.AddressUserRepository;
import com.fiap.techchallenge.application.services.user.AddressUserUseCase;
import com.fiap.techchallenge.infrastructure.validation.CreateAddressValidator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AddressUserUseCaseImpl implements AddressUserUseCase {

    private final AddressUserRepository addressUserRepository;
    private final CreateAddressValidator createAddressValidator;

    public AddressUserUseCaseImpl(AddressUserRepository addressUserRepository,
            CreateAddressValidator createAddressValidator) {
        this.addressUserRepository = addressUserRepository;
        this.createAddressValidator = createAddressValidator;
    }

    @Override
    public Address create(CreateAddress createAddress) {
        createAddressValidator.validate(createAddress);
        return this.addressUserRepository.create(createAddress);
    }

    @Override
    public List<Address> listAll(UUID idUser) {
        return this.addressUserRepository.listAll(idUser);
    }

    @Override
    public Address update(UpdateAddress updateAddress, UUID userId, UUID addressId) {
        return this.addressUserRepository.update(updateAddress, userId, addressId);
    }

    @Override
    public void delete(UUID idUSer, UUID idAddress) {
        this.addressUserRepository.delete(idUSer, idAddress);
    }
}
