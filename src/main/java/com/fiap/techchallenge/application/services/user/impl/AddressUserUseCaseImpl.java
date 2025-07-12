package com.fiap.techchallenge.application.services.user.impl;

import com.fiap.techchallenge.application.ports.in.user.dtos.Address;
import com.fiap.techchallenge.application.ports.in.user.dtos.CreateAddress;
import com.fiap.techchallenge.application.ports.in.user.dtos.UpdateAddress;
import com.fiap.techchallenge.application.ports.out.user.AddressUserRepository;
import com.fiap.techchallenge.application.services.user.AddressUserUseCase;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AddressUserUseCaseImpl implements AddressUserUseCase {

    private final AddressUserRepository addressUserRepository;

    public AddressUserUseCaseImpl(AddressUserRepository addressUserRepository) {
        this.addressUserRepository = addressUserRepository;
    }

    @Override
    public Address create(CreateAddress createAddress) {
        return this.addressUserRepository.create(createAddress);
    }

    @Override
    public List<Address> listAll(UUID idUser) {
        return this.addressUserRepository.listAll(idUser);
    }

    @Override
    public Address update(UpdateAddress updateAddress) {
        return this.addressUserRepository.update(updateAddress);
    }

    @Override
    public void delete(UUID idUSer, UUID idAddress) {

    }
}
