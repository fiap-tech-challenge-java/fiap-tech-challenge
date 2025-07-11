package com.fiap.techchallenge.infrastructure.adapters.out.persistence.user.mapper;

import com.fiap.techchallenge.application.ports.in.user.dtos.CreateUser;
import com.fiap.techchallenge.application.ports.in.user.dtos.User;
import com.fiap.techchallenge.infrastructure.adapters.out.persistence.user.entities.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(source = "addressesList", target = "address")
    User mapToUser(UserEntity userEntity);

    UserEntity mapToEntity(CreateUser createUser);
}
