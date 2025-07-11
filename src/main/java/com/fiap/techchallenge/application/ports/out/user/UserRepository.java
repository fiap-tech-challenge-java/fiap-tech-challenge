package com.fiap.techchallenge.application.ports.out.user;

import com.fiap.techchallenge.application.ports.in.user.dtos.ChangePassword;
import com.fiap.techchallenge.application.ports.in.user.dtos.CreateUser;
import com.fiap.techchallenge.application.ports.in.user.dtos.UpdateUser;
import com.fiap.techchallenge.application.ports.in.user.dtos.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    User create(CreateUser user);

    Optional<User> findById(UUID id);

    Optional<User> findByLogin(String login);

    List<User> findAll();

    User update(UUID id, UpdateUser updateUser);

    void deleteById(UUID id);

    void changePassword(ChangePassword changePassword);

    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}
