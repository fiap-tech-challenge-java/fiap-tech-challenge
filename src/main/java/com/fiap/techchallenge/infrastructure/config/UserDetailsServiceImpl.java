package com.fiap.techchallenge.infrastructure.config;

import com.fiap.techchallenge.application.ports.in.user.dtos.User;
import com.fiap.techchallenge.infrastructure.adapters.out.persistence.user.UserDataSource;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@Primary
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserDataSource userDataSource;

    public UserDetailsServiceImpl(UserDataSource userDataSource) {
        this.userDataSource = userDataSource;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDataSource.findByLogin(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(user.getRoleEnum().name()));

        return new org.springframework.security.core.userdetails.User(user.getLogin(), user.getPassword(), authorities);
    }
}
