package com.fiap.techchallenge.infrastructure.adapters.out.persistence.user;

import com.fiap.techchallenge.application.ports.in.user.dtos.Address;
import com.fiap.techchallenge.application.ports.in.user.dtos.ChangePassword;
import com.fiap.techchallenge.application.ports.in.user.dtos.CreateUser;
import com.fiap.techchallenge.application.ports.in.user.dtos.UpdateUser;
import com.fiap.techchallenge.application.ports.in.user.dtos.User;
import com.fiap.techchallenge.application.ports.out.user.UserRepository;
import com.fiap.techchallenge.domain.exceptions.BusinessException;
import com.fiap.techchallenge.domain.exceptions.UserNotFoundException;
import com.fiap.techchallenge.domain.model.enums.RoleEnum;
import com.fiap.techchallenge.domain.utils.UsernameValidator;
import com.fiap.techchallenge.infrastructure.adapters.out.persistence.user.entities.AddressUserEntity;
import com.fiap.techchallenge.infrastructure.adapters.out.persistence.user.entities.UserEntity;
import com.fiap.techchallenge.infrastructure.adapters.out.persistence.user.mapper.UserMapper;
import com.fiap.techchallenge.infrastructure.adapters.out.persistence.user.repositories.UserJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserDataSourceTest {

    @Mock
    private UserJpaRepository jpaRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UsernameValidator usernameValidator;

    @InjectMocks
    private UserDataSource userDataSource;

    private CreateUser createUser;
    private UserEntity savedEntity;

    @BeforeEach
    void setUp() {
        createUser = new CreateUser();
        createUser.setLogin("testLogin");
        createUser.setPassword("plain");
        createUser.setEmail("email@example.com");
        createUser.setName("Test Name");
        createUser.setCpf("12345678901");
        createUser.setRole(RoleEnum.CUSTOMER);

        // Prepare a default entity returned by save
        savedEntity = new UserEntity();
        savedEntity.setId(UUID.randomUUID());
        savedEntity.setLogin(createUser.getLogin());
        savedEntity.setPassword("encoded");
        savedEntity.setEmail(createUser.getEmail());
        savedEntity.setName(createUser.getName());
        savedEntity.setCpf(createUser.getCpf());
        savedEntity.setRole(savedEntity.getRole()); // depends on default from mapper
        savedEntity.setActive(true);
        savedEntity.setAddressesList(List.of());
    }

    @Test
    void shouldCreateUserWithAddressSuccessfully() {
        // Arrange
        Address addressDto = new Address();
        addressDto.setPublicPlace("Street");
        addressDto.setNumber("42");
        addressDto.setComplement("Apt");
        addressDto.setNeighborhood("Neighborhood");
        addressDto.setCity("City");
        addressDto.setState("State");
        addressDto.setPostalCode("00000-000");
        createUser.setAddress(addressDto);

        when(passwordEncoder.encode("plain")).thenReturn("encoded");
        // simulate that save returns entity with address added
        AddressUserEntity addressEntity = new AddressUserEntity(null, "Street", "42", "Apt", "Neighborhood", "City",
                "State", "00000-000", savedEntity);
        savedEntity.setAddressesList(List.of(addressEntity));
        when(jpaRepository.save(any(UserEntity.class))).thenReturn(savedEntity);

        // Act
        User result = userDataSource.create(createUser);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getAddress().size());
        assertEquals("Street", result.getAddress().get(0).getPublicPlace());
        verify(passwordEncoder).encode("plain");
        verify(jpaRepository).save(any(UserEntity.class));
    }

    @Test
    void shouldFindByIdSuccessfully() {
        // Arrange
        UUID id = UUID.randomUUID();
        UserEntity entity = new UserEntity();
        entity.setId(id);
        when(jpaRepository.findById(id)).thenReturn(Optional.of(entity));

        // Act
        Optional<User> result = userDataSource.findById(id);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(id, result.get().getId());
    }

    @Test
    void shouldThrowBusinessExceptionWhenFindByIdNotFound() {
        // Arrange
        UUID id = UUID.randomUUID();
        when(jpaRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        BusinessException ex = assertThrows(BusinessException.class, () -> userDataSource.findById(id));
        assertTrue(ex.getMessage().contains("User not found with id:"));
    }

    @Test
    void shouldFindByIdOnlyWhenFound() {
        // Arrange
        UUID id = UUID.randomUUID();
        UserEntity entity = new UserEntity();
        entity.setId(id);
        entity.setLogin("login");
        entity.setPassword("pwd");
        entity.setEmail("e@e.com");
        entity.setRole(entity.getRole());
        entity.setActive(true);
        when(jpaRepository.findById(id)).thenReturn(Optional.of(entity));

        // Act
        Optional<User> result = userDataSource.findByIdOnly(id);

        // Assert
        assertTrue(result.isPresent());
        User u = result.get();
        assertEquals(id, u.getId());
        assertEquals("login", u.getLogin());
        assertEquals("pwd", u.getPassword());
        assertEquals("e@e.com", u.getEmail());
        assertTrue(u.isActive());
    }

    @Test
    void shouldReturnEmptyOptionalWhenFindByIdOnlyNotFound() {
        // Arrange
        UUID id = UUID.randomUUID();
        when(jpaRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        Optional<User> result = userDataSource.findByIdOnly(id);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldFindByLoginWhenFound() {
        // Arrange
        String login = "user";
        UserEntity entity = new UserEntity();
        entity.setLogin(login);
        when(jpaRepository.findByLogin(login)).thenReturn(Optional.of(entity));

        // Act
        Optional<User> result = userDataSource.findByLogin(login);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(login, result.get().getLogin());
    }

    @Test
    void shouldReturnEmptyOptionalWhenFindByLoginNotFound() {
        // Arrange
        String login = "user";
        when(jpaRepository.findByLogin(login)).thenReturn(Optional.empty());

        // Act
        Optional<User> result = userDataSource.findByLogin(login);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldFindAllUsersSuccessfully() {
        // Arrange
        UserEntity e1 = new UserEntity();
        e1.setId(UUID.randomUUID());
        UserEntity e2 = new UserEntity();
        e2.setId(UUID.randomUUID());
        when(jpaRepository.findAll()).thenReturn(List.of(e1, e2));

        // Act
        List<User> result = userDataSource.findAll();

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(u -> u.getId().equals(e1.getId())));
        assertTrue(result.stream().anyMatch(u -> u.getId().equals(e2.getId())));
    }

    @Test
    void shouldUpdateUserSuccessfully() {
        // Arrange
        UUID id = UUID.randomUUID();
        UpdateUser update = new UpdateUser("newLogin", "newName", "newEmail@email.com");
        update.setLogin("newLogin");
        update.setName("newName");
        update.setEmail("newEmail@email.com");

        UserEntity entity = new UserEntity();
        entity.setId(id);
        when(jpaRepository.findById(id)).thenReturn(Optional.of(entity));

        // Act
        userDataSource.update(id, update);

        // Assert
        ArgumentCaptor<UserEntity> captor = ArgumentCaptor.forClass(UserEntity.class);
        verify(usernameValidator).validate("newLogin");
        verify(jpaRepository).save(captor.capture());
        assertEquals("newLogin", captor.getValue().getLogin());
        assertEquals("newName", captor.getValue().getName());
    }

    @Test
    void shouldThrowUserNotFoundExceptionWhenUpdateWithInvalidId() {
        // Arrange
        UUID id = UUID.randomUUID();
        when(jpaRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class,
                () -> userDataSource.update(id, new UpdateUser("newLogin", "newName", "newEmail@email.com")));
    }

    @Test
    void shouldThrowBusinessExceptionWhenUsernameInvalidOnUpdate() {
        // Arrange
        UUID id = UUID.randomUUID();
        UpdateUser update = new UpdateUser("newLogin", "newName", "newEmail@email.com");
        update.setEmail("email@email");
        update.setLogin("badLogin");
        update.setName("newName");

        UserEntity entity = new UserEntity();
        entity.setId(id);
        when(jpaRepository.findById(id)).thenReturn(Optional.of(entity));
        doThrow(new BusinessException("Invalid")).when(usernameValidator).validate("badLogin");

        // Act & Assert
        assertThrows(BusinessException.class, () -> userDataSource.update(id, update));
        verify(jpaRepository, never()).save(any());
    }

    @Test
    void shouldDeleteByIdSuccessfully() {
        // Arrange
        UUID id = UUID.randomUUID();
        UserEntity entity = new UserEntity();
        entity.setId(id);
        entity.setActive(true);
        when(jpaRepository.findById(id)).thenReturn(Optional.of(entity));

        // Act
        userDataSource.deleteById(id);

        // Assert
        assertFalse(entity.isActive());
        verify(jpaRepository).save(entity);
    }

    @Test
    void shouldThrowUserNotFoundExceptionWhenDeleteByIdInvalid() {
        // Arrange
        UUID id = UUID.randomUUID();
        when(jpaRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> userDataSource.deleteById(id));
    }

    @Test
    void shouldChangePasswordSuccessfully() {
        // Arrange
        UUID id = UUID.randomUUID();
        ChangePassword cp = new ChangePassword();
        cp.setId(id);
        cp.setNewPassword("np");
        UserEntity entity = new UserEntity();
        entity.setId(id);
        when(jpaRepository.findById(id)).thenReturn(Optional.of(entity));

        // Act
        userDataSource.changePassword(cp);

        // Assert
        assertEquals("np", entity.getPassword());
        verify(jpaRepository).save(entity);
    }

    @Test
    void shouldThrowUserNotFoundExceptionWhenChangePasswordInvalid() {
        // Arrange
        UUID id = UUID.randomUUID();
        ChangePassword cp = new ChangePassword();
        cp.setId(id);
        cp.setNewPassword("np");
        when(jpaRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> userDataSource.changePassword(cp));
    }

    @Test
    void shouldRecoverPasswordSuccessfully() {
        // Arrange
        UUID id = UUID.randomUUID();
        when(jpaRepository.findPasswordById(id)).thenReturn(Optional.of("secret"));

        // Act
        String pwd = userDataSource.recoverPassword(id);

        // Assert
        assertEquals("secret", pwd);
    }

    @Test
    void shouldThrowUserNotFoundExceptionWhenRecoverPasswordInvalid() {
        // Arrange
        UUID id = UUID.randomUUID();
        when(jpaRepository.findPasswordById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> userDataSource.recoverPassword(id));
    }
}
