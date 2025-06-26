package com.fiap.techchallenge.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private UUID id;
    private String name;
    private String email;
    private String login;
    private String password;
    private LocalDateTime lastModifiedDate;
    private String address;
    private UserType type;

    public enum UserType {
        CLIENTE, DONO_RESTAURANTE
    }

    public User(String name, String email, String login, String password, String address, UserType type) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.email = email;
        this.login = login;
        this.password = password;
        this.address = address;
        this.type = type;
        this.lastModifiedDate = LocalDateTime.now();
    }
}
