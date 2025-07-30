package com.fiap.techchallenge.infrastructure.adapters.out.persistence.user.mapper;

import com.fiap.techchallenge.application.ports.in.user.dtos.Address;
import com.fiap.techchallenge.application.ports.in.user.dtos.CreateAddress;
import com.fiap.techchallenge.application.ports.in.user.dtos.UpdateAddress;
import com.fiap.techchallenge.infrastructure.adapters.out.persistence.user.entities.AddressUserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface AddressUserMapper {
    AddressUserMapper INSTANCE = Mappers.getMapper(AddressUserMapper.class);

    AddressUserEntity mapToEntity(CreateAddress createAddress);

    List<Address> mapToAddressList(List<AddressUserEntity> entities);

    Address mapToAddress(AddressUserEntity entity);
}
