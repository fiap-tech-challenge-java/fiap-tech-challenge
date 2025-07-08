package com.fiap.techchallenge.application.ports.in.user.dtos;

import com.fiap.techchallenge.domain.model.enums.RoleEnum;
import com.fiap.techchallenge.model.Address;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.UUID;

public class User {

    private UUID id;
    private String name;
    private String email;
    private String login;
    private String password;
    private OffsetDateTime updatedAt;
    private OffsetDateTime createdAt;
    private Address address;
    private RoleEnum role;
    private boolean active;

    public User() {
    }

    public User(UUID id, String name, String email, String login, String password, LocalDateTime lastModifiedDate,
            Address address, RoleEnum role, boolean active, OffsetDateTime updatedAt, OffsetDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.login = login;
        this.password = password;
        this.address = address;
        this.role = role;
        this.active = active;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public RoleEnum getRole() {
        return role;
    }

    public void setRole(RoleEnum role) {
        this.role = role;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
