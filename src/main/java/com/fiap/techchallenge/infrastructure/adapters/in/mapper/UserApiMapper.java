package com.fiap.techchallenge.infrastructure.adapters.in.mapper;

import com.fiap.techchallenge.application.ports.in.user.dtos.ChangePassword;
import com.fiap.techchallenge.application.ports.in.user.dtos.CreateUser;
import com.fiap.techchallenge.application.ports.in.user.dtos.UpdateUser;
import com.fiap.techchallenge.application.ports.in.user.dtos.User;
import com.fiap.techchallenge.model.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

@Mapper(componentModel = "spring")
public interface UserApiMapper {

    UserApiMapper INSTANCE = Mappers.getMapper(UserApiMapper.class);

    CreateUser mapToCreateUser(CreateUserRequest userRequest);

    @Mapping(source = "createdAt", target = "createdAt", qualifiedByName = "formatOffsetDateTime")
    @Mapping(source = "updatedAt", target = "updatedAt", qualifiedByName = "formatOffsetDateTime")
    CreateUserResponse mapToCreateUserResponse(User user);

    @Mapping(source = "createdAt", target = "createdAt", qualifiedByName = "formatOffsetDateTime")
    @Mapping(source = "updatedAt", target = "updatedAt", qualifiedByName = "formatOffsetDateTime")
    UserResponse mapToUserResponse(User user);

    UpdateUser mapToUpdateUser(UpdateUserRequest updateUser);

    ChangePassword mapToChangePassword(ChangePasswordRequest changePasswordRequest);

    @Named("formatOffsetDateTime")
    static String formatOffsetDateTime(OffsetDateTime dateTime) {
        if (dateTime == null)
            return null;
        return dateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
    }
}
