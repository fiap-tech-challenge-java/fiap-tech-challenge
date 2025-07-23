package com.fiap.techchallenge.infrastructure.adapters.in.web;

import com.fiap.techchallenge.application.ports.in.user.dtos.CreateUser;
import com.fiap.techchallenge.application.ports.in.user.dtos.UpdateUser;
import com.fiap.techchallenge.application.services.user.UserUseCase;
import com.fiap.techchallenge.infrastructure.adapters.in.mapper.UserApiMapper;
import com.fiap.techchallenge.model.CreateUserRequest;
import com.fiap.techchallenge.model.CreateUserResponse;
import com.fiap.techchallenge.model.UpdateUserRequest;
import com.fiap.techchallenge.model.UserResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserApiImplTest {

    @Mock
    private UserUseCase userUseCase;

    @InjectMocks
    private UserApiImpl userApiImpl;

    private UUID userId;

    private CreateUserRequest createUserRequest;
    private com.fiap.techchallenge.application.ports.in.user.dtos.User domainUser;
    private CreateUserResponse expectedCreateResponse;

    private UpdateUserRequest updateUserRequest;
    private com.fiap.techchallenge.application.ports.in.user.dtos.User updatedDomainUser;
    private UserResponse expectedUpdateResponse;

    @BeforeEach
    void setUp() {
        // Gera um ID de usuário
        userId = UUID.randomUUID();

        // Limpa qualquer autenticação anterior
        SecurityContextHolder.clearContext();

        // --- setup para createUser ---
        createUserRequest = new CreateUserRequest();
        createUserRequest.setLogin("newLogin");
        createUserRequest.setPassword("newPassword");
        createUserRequest.setEmail("newEmail@example.com");
        createUserRequest.setName("New Name");
        createUserRequest.setCpf("12345678901");
        createUserRequest.setRole(CreateUserRequest.RoleEnum.CUSTOMER);

        createUserRequest.setAddress(new com.fiap.techchallenge.model.AddressUserRequest());

        domainUser = new com.fiap.techchallenge.application.ports.in.user.dtos.User();
        domainUser.setId(userId);
        domainUser.setLogin("newLogin");
        domainUser.setPassword("newPassword");
        domainUser.setEmail("newEmail@example.com");
        domainUser.setName("New Name");
        domainUser.setCpf("12345678901");

        expectedCreateResponse = UserApiMapper.INSTANCE.mapToCreateUserResponse(domainUser);

        // --- setup para updateUser ---
        updateUserRequest = new UpdateUserRequest();
        updateUserRequest.setLogin("updatedLogin");
        updateUserRequest.setEmail("updatedEmail@example.com");
        updateUserRequest.setName("Updated Name");

        updatedDomainUser = new com.fiap.techchallenge.application.ports.in.user.dtos.User();
        updatedDomainUser.setId(userId);
        updatedDomainUser.setLogin("updatedLogin");
        updatedDomainUser.setEmail("updatedEmail@example.com");
        updatedDomainUser.setName("Updated Name");
        updatedDomainUser.setCpf("10987654321");

        expectedUpdateResponse = UserApiMapper.INSTANCE.mapToUserResponse(updatedDomainUser);
    }

    @Test
    void shouldCreateUserSuccessfully() {
        when(userUseCase.create(any(CreateUser.class))).thenReturn(domainUser);

        // Act
        ResponseEntity<CreateUserResponse> response =
                userApiImpl.createUser(createUserRequest);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(expectedCreateResponse, response.getBody());

        ArgumentCaptor<CreateUser> captor = ArgumentCaptor.forClass(CreateUser.class);
        verify(userUseCase).create(captor.capture());
        CreateUser actual = captor.getValue();
        assertEquals(createUserRequest.getLogin(),    actual.getLogin());
        assertEquals(createUserRequest.getPassword(), actual.getPassword());
        assertEquals(createUserRequest.getEmail(),    actual.getEmail());
        assertEquals(createUserRequest.getName(),     actual.getName());
        assertEquals(createUserRequest.getCpf(),      actual.getCpf());
    }

}
