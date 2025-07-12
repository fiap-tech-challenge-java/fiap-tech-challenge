package com.fiap.techchallenge.infrastructure.adapters.out.persistence.user;

import com.fiap.techchallenge.application.ports.in.user.dtos.Address;
import com.fiap.techchallenge.application.ports.in.user.dtos.CreateAddress;
import com.fiap.techchallenge.application.ports.in.user.dtos.UpdateAddress;
import com.fiap.techchallenge.application.ports.out.user.AddressUserRepository;
import com.fiap.techchallenge.domain.exceptions.AddressNotFoundException;
import com.fiap.techchallenge.domain.exceptions.UserNotFoundException;
import com.fiap.techchallenge.infrastructure.adapters.in.mapper.AddressUserApiMapper;
import com.fiap.techchallenge.infrastructure.adapters.out.persistence.user.entities.AddressUserEntity;
import com.fiap.techchallenge.infrastructure.adapters.out.persistence.user.entities.UserEntity;
import com.fiap.techchallenge.infrastructure.adapters.out.persistence.user.mapper.AddressUserMapper;
import com.fiap.techchallenge.infrastructure.adapters.out.persistence.user.repositories.AddressUserJpaRepository;
import com.fiap.techchallenge.infrastructure.adapters.out.persistence.user.repositories.UserJpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AddressUserDataSource implements AddressUserRepository {

    private final AddressUserJpaRepository addressUserJpaRepository;
    private final UserJpaRepository userJpaRepository;

    private static final AddressUserMapper ADDRESS_USER_MAPPER = AddressUserMapper.INSTANCE;

    public AddressUserDataSource(AddressUserJpaRepository addressUserJpaRepository,
            UserJpaRepository userJpaRepository) {
        this.addressUserJpaRepository = addressUserJpaRepository;
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    public Address create(CreateAddress createAddress) {
        UserEntity user = userJpaRepository.findById(createAddress.getIdUser()).orElseThrow(UserNotFoundException::new);

        AddressUserEntity entity = ADDRESS_USER_MAPPER.mapToEntity(createAddress);
        entity.setUser(user);

        AddressUserEntity saved = addressUserJpaRepository.save(entity);

        return AddressUserApiMapper.INSTANCE.mapToAddress(saved);
    }

    @Override
    public List<Address> listAll(UUID userId) {
        List<AddressUserEntity> entities = addressUserJpaRepository.findAllByUserId(userId);

        return ADDRESS_USER_MAPPER.mapToAddressList(entities);
    }

    @Override
    public Address update(UpdateAddress updateAddress) {
        userJpaRepository.findById(updateAddress.getIdUser()).orElseThrow(UserNotFoundException::new);

        AddressUserEntity entity = addressUserJpaRepository
                .findByIdAndUserId(updateAddress.getIdAddress(), updateAddress.getIdUser())
                .orElseThrow(AddressNotFoundException::new);

        entity.setPublicPlace(updateAddress.getPublicPlace());
        entity.setNumber(updateAddress.getNumber());
        entity.setComplement(updateAddress.getComplement());
        entity.setNeighborhood(updateAddress.getNeighborhood());
        entity.setCity(updateAddress.getCity());
        entity.setState(updateAddress.getState());
        entity.setPostalCode(updateAddress.getPostalCode());

        AddressUserEntity saved = addressUserJpaRepository.save(entity);

        return ADDRESS_USER_MAPPER.mapToAddress(saved);
    }

}
