package com.fiap.techchallenge.application.services.user.impl;

import com.fiap.techchallenge.application.ports.in.user.dtos.*;
import com.fiap.techchallenge.application.ports.out.user.UserRepository;
import com.fiap.techchallenge.infrastructure.validation.ChangePasswordValidator;
import com.fiap.techchallenge.infrastructure.validation.CreateUserValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserUseCaseImplTest {
    @Mock
    private CreateUserValidator createUserValidator;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private ChangePasswordValidator changePasswordValidator;

    @InjectMocks
    private UserUseCaseImpl userUseCase;

    private CreateUser createUser;
    private User user;
    private UpdateUser updateUser;
    private ChangePassword changePassword;
    private UUID userId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();

        // Cria o DTO de criação com setters
        createUser = new CreateUser();
        createUser.setName("name");
        createUser.setEmail("email@email.com");
        createUser.setCpf("12345678901");
        createUser.setLogin("login");
        createUser.setPassword("password");
        createUser.setAddress(null); // ou preencha se tiver endereço

        // Simula o usuário de retorno do repositório
        user = new User();
        user.setId(userId);
        user.setLogin("login");
        user.setEmail(createUser.getEmail());
        user.setName(createUser.getName());
        user.setCpf(createUser.getCpf());
        user.setAddress(Collections.emptyList());
        user.setActive(true);

        // Cria o DTO de atualização com setters
        updateUser = new UpdateUser("newLogin", "newName", "newEmail@email.com");
        updateUser.setLogin("newLogin");
        updateUser.setName("newName");
        updateUser.setEmail("newEmail");

        // Cria o DTO de troca de senha
        changePassword = new ChangePassword();
        changePassword.setId(userId);
        changePassword.setNewPassword("newPassword");
    }

    @Test
    void shouldCreateUserSuccessfully() {
        // Arrange
        doNothing().when(createUserValidator).validate(createUser);
        when(userRepository.create(createUser)).thenReturn(user);
        // Act
        User result = userUseCase.create(createUser);
        // Assert
        assertEquals(user, result);
        verify(createUserValidator).validate(createUser);
        verify(userRepository).create(createUser);
    }

    @Test
    void shouldThrowExceptionWhenCreateUserInvalid() {
        // Arrange
        doThrow(new IllegalArgumentException("Invalid")).when(createUserValidator).validate(createUser);
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userUseCase.create(createUser));
        verify(createUserValidator).validate(createUser);
        verify(userRepository, never()).create(any());
    }

    @Test
    void shouldGetAllUsersSuccessfully() {
        // Arrange
        List<User> users = Arrays.asList(user);
        when(userRepository.findAll()).thenReturn(users);
        // Act
        List<User> result = userUseCase.getAll();
        // Assert
        assertEquals(users, result);
        verify(userRepository).findAll();
    }

    @Test
    void shouldGetUserByIdSuccessfully() {
        // Arrange
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        // Act
        User result = userUseCase.getById(userId);
        // Assert
        assertEquals(user, result);
        verify(userRepository).findById(userId);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundById() {
        // Arrange
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        // Act & Assert
        Exception ex = assertThrows(IllegalArgumentException.class, () -> userUseCase.getById(userId));
        assertTrue(ex.getMessage().contains("User not found"));
        verify(userRepository).findById(userId);
    }

    @Test
    void shouldUpdateUserSuccessfully() {
        // Arrange
        when(userRepository.update(userId, updateUser)).thenReturn(user);
        // Act
        User result = userUseCase.update(userId, updateUser);
        // Assert
        assertEquals(user, result);
        verify(userRepository).update(userId, updateUser);
    }

    @Test
    void shouldDeleteUserSuccessfully() {
        // Arrange
        doNothing().when(userRepository).deleteById(userId);
        // Act
        userUseCase.delete(userId);
        // Assert
        verify(userRepository).deleteById(userId);
    }

    @Test
    void shouldChangePasswordSuccessfully() {
        // Arrange
        String oldPasswordEncoded = "encodedOldPassword";
        when(userRepository.recoverPassword(userId)).thenReturn(oldPasswordEncoded);
        doNothing().when(changePasswordValidator).isValid(changePassword, oldPasswordEncoded);
        when(passwordEncoder.encode(changePassword.getNewPassword())).thenReturn("encodedNewPassword");
        doNothing().when(userRepository).changePassword(any(ChangePassword.class));
        // Act
        userUseCase.changePassword(changePassword);
        // Assert
        verify(userRepository).recoverPassword(userId);
        verify(changePasswordValidator).isValid(changePassword, oldPasswordEncoded);
        verify(passwordEncoder).encode(changePassword.getNewPassword());
        verify(userRepository).changePassword(any(ChangePassword.class));
    }

    @Test
    void shouldThrowExceptionWhenChangePasswordInvalid() {
        // Arrange
        String oldPasswordEncoded = "encodedOldPassword";
        when(userRepository.recoverPassword(userId)).thenReturn(oldPasswordEncoded);
        doThrow(new IllegalArgumentException("Invalid password")).when(changePasswordValidator).isValid(changePassword,
                oldPasswordEncoded);
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userUseCase.changePassword(changePassword));
        verify(userRepository).recoverPassword(userId);
        verify(changePasswordValidator).isValid(changePassword, oldPasswordEncoded);
        verify(passwordEncoder, never()).encode(any());
        verify(userRepository, never()).changePassword(any());
    }

    @Test
    void shouldThrowExceptionWhenCreatingUserWithNullInput() {
        // Arrange - Mock the validator to throw NPE when validate is called with null
        doThrow(new NullPointerException("CreateUser cannot be null")).when(createUserValidator).validate(null);

        // Act & Assert
        assertThrows(NullPointerException.class, () -> userUseCase.create(null));

        // Verify the validator was called with null
        verify(createUserValidator).validate(null);
        verify(userRepository, never()).create(any());
    }

    @Test
    void shouldThrowExceptionWhenUpdateUserNotFound() {
        // Arrange
        when(userRepository.update(userId, updateUser)).thenThrow(new IllegalArgumentException("User not found"));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userUseCase.update(userId, updateUser));
        verify(userRepository).update(userId, updateUser);
    }

    @Test
    void shouldHandleDeleteNonExistentUser() {
        // Arrange
        doThrow(new IllegalArgumentException("User not found")).when(userRepository).deleteById(userId);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userUseCase.delete(userId));
        verify(userRepository).deleteById(userId);
    }

    @Test
    void shouldReturnEmptyListWhenNoUsersExist() {
        // Arrange
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<User> result = userUseCase.getAll();

        // Assert
        assertTrue(result.isEmpty());
        verify(userRepository).findAll();
    }

    @Test
    void shouldThrowExceptionWhenChangePasswordForNonExistentUser() {
        // Arrange
        when(userRepository.recoverPassword(userId)).thenThrow(new IllegalArgumentException("User not found"));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userUseCase.changePassword(changePassword));
        verify(userRepository).recoverPassword(userId);
        verify(changePasswordValidator, never()).isValid(any(), any());
        verify(passwordEncoder, never()).encode(any());
        verify(userRepository, never()).changePassword(any());
    }
}