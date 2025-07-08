package com.fiap.techchallenge.infrastructure.adapters.in.mapper;

import com.fiap.techchallenge.application.ports.in.user.dtos.Address;
import com.fiap.techchallenge.application.ports.in.user.dtos.CreateAddress;
import com.fiap.techchallenge.application.ports.in.user.dtos.UpdateAddress;
import com.fiap.techchallenge.infrastructure.adapters.out.persistence.user.entities.AddressUserEntity;
import com.fiap.techchallenge.model.AddressResponse;
import com.fiap.techchallenge.model.CreateAddressRequest;
import com.fiap.techchallenge.model.UpdateAddressRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface AddressUserApiMapper {

    AddressUserApiMapper INSTANCE = Mappers.getMapper(AddressUserApiMapper.class);

    CreateAddress mapToCreateAddress(CreateAddressRequest createAddressRequest);

    Address mapToAddress(AddressUserEntity entity);

    AddressResponse mapToAddressResponse(Address address);

    List<AddressResponse> mapToAddressResponseList(List<Address> addresses);

    UpdateAddress mapToUpdateAddress(UpdateAddressRequest request);

}
