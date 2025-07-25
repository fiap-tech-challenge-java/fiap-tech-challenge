package com.fiap.techchallenge.infrastructure.adapters.in.web;

import com.fiap.techchallenge.application.ports.in.user.dtos.CreateUser;
import com.fiap.techchallenge.application.ports.in.user.dtos.UpdateUser;
import com.fiap.techchallenge.application.ports.in.user.dtos.User;
import com.fiap.techchallenge.application.services.user.UserUseCase;
import com.fiap.techchallenge.infrastructure.adapters.in.mapper.UserApiMapper;
import com.fiap.techchallenge.infrastructure.config.UserDetailsImpl;
import com.fiap.techchallenge.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

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
    private User domainUser;
    private CreateUserResponse expectedCreateResponse;
    private UserResponse expectedUpdateResponse;

    private UpdateUserRequest updateUserRequest;
    private User updatedDomainUser;

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

        createUserRequest.setAddress(new AddressUserRequest());

        domainUser = new User();
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

        updatedDomainUser = new User();
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
        ResponseEntity<CreateUserResponse> response = userApiImpl.createUser(createUserRequest);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(expectedCreateResponse, response.getBody());

        ArgumentCaptor<CreateUser> captor = ArgumentCaptor.forClass(CreateUser.class);
        verify(userUseCase).create(captor.capture());
        CreateUser actual = captor.getValue();
        assertEquals(createUserRequest.getLogin(), actual.getLogin());
        assertEquals(createUserRequest.getPassword(), actual.getPassword());
        assertEquals(createUserRequest.getEmail(), actual.getEmail());
        assertEquals(createUserRequest.getName(), actual.getName());
        assertEquals(createUserRequest.getCpf(), actual.getCpf());
    }

    @Test
    void shouldUpdateUserSuccessfully() {
        // Arrange
        // Simula usuário autenticado igual ao userId (deve permitir update)
        UserDetailsImpl userDetails = mock(UserDetailsImpl.class);
        when(userDetails.getId()).thenReturn(userId);
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(userUseCase.update(eq(userId), any())).thenReturn(updatedDomainUser);

        // Act
        ResponseEntity<UserResponse> response = userApiImpl.updateUser(userId, updateUserRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedUpdateResponse, response.getBody());

        ArgumentCaptor<UpdateUser> captor = ArgumentCaptor.forClass(UpdateUser.class);
        verify(userUseCase).update(eq(userId), captor.capture());
        UpdateUser actual = captor.getValue();
        assertEquals(updateUserRequest.getLogin(), actual.getLogin());
        assertEquals(updateUserRequest.getEmail(), actual.getEmail());
        assertEquals(updateUserRequest.getName(), actual.getName());
    }

}
