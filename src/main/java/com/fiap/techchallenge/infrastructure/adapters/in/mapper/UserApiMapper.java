package com.fiap.techchallenge.infrastructure.adapters.in.mapper;

import com.fiap.techchallenge.application.ports.in.user.dtos.ChangePassword;
import com.fiap.techchallenge.application.ports.in.user.dtos.CreateUser;
import com.fiap.techchallenge.application.ports.in.user.dtos.UpdateUser;
import com.fiap.techchallenge.application.ports.in.user.dtos.User;
import com.fiap.techchallenge.model.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserApiMapper {

    UserApiMapper INSTANCE = Mappers.getMapper(UserApiMapper.class);

    @Mapping(source = "role", target = "roleEnum")
    CreateUser mapToCreateUser(CreateUserRequest userRequest);

    CreateUserResponse mapToCreateUserResponse(User user);

    UserResponse mapToUserResponse(User user);

    UpdateUser mapToUpdateUser(UpdateUserRequest updateUser);

    ChangePassword mapToChangePassword(ChangePasswordRequest changePasswordRequest);
}
