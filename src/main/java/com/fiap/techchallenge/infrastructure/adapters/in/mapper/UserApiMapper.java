package com.fiap.techchallenge.infrastructure.adapters.in.mapper;

import com.fiap.techchallenge.application.ports.in.user.dtos.ChangePassword;
import com.fiap.techchallenge.application.ports.in.user.dtos.CreateUser;
import com.fiap.techchallenge.application.ports.in.user.dtos.User;
import com.fiap.techchallenge.model.ChangePasswordRequest;
import com.fiap.techchallenge.model.UserRequest;
import com.fiap.techchallenge.model.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserApiMapper {

    UserApiMapper INSTANCE = Mappers.getMapper(UserApiMapper.class);

    CreateUser mapToCreateUser(UserRequest userRequest);

    UserResponse mapToUserResponse(User user);

    ChangePassword mapToChangePassword(ChangePasswordRequest changePasswordRequest);
}
