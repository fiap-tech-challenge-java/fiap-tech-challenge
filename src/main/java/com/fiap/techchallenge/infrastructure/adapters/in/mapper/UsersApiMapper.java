package com.fiap.techchallenge.infrastructure.adapters.in.mapper;

import com.fiap.techchallenge.application.ports.in.user.dtos.CreateUser;
import com.fiap.techchallenge.application.ports.in.user.dtos.User;
import com.fiap.techchallenge.user.model.UserRequest;
import com.fiap.techchallenge.user.model.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UsersApiMapper {

    UsersApiMapper INSTANCE = Mappers.getMapper(UsersApiMapper.class);

    CreateUser mapToCreateUser(UserRequest userRequest);

    UserResponse mapToUserResponse(User user);
}
