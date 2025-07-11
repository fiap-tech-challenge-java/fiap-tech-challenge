package com.fiap.techchallenge.infrastructure.adapters.in.mapper;

import com.fiap.techchallenge.infrastructure.adapters.out.persistence.user.entities.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class UserDetailsMapper {

    public UserDetails mapToUserDetails(UserEntity userEntity) {
        Set<GrantedAuthority> authorities = Set.of(new SimpleGrantedAuthority(userEntity.getRole().name()));

        return new org.springframework.security.core.userdetails.User(userEntity.getLogin(), userEntity.getPassword(),
                userEntity.isActive(), true, true, true, authorities);
    }
}
