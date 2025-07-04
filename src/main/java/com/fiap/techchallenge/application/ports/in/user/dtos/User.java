package com.fiap.techchallenge.application.ports.in.user.dtos;

import com.fiap.techchallenge.domain.model.enums.RoleEnum;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class User {

    private UUID id;
    private String name;
    private String email;
    private String cpf;
    private String login;
    private String password;
    private List<Address> address;
    private RoleEnum roleEnum;
    private boolean active;

    public User() {
    }

    public User(UUID id, String name, String email, String cpf, String login, String password, List<Address> address,
            RoleEnum roleEnum, boolean active) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.cpf = cpf;
        this.login = login;
        this.password = password;
        this.address = address;
        this.roleEnum = roleEnum;
        this.active = active;
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

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
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

    public List<Address> getAddress() {
        return address;
    }

    public void setAddress(List<Address> address) {
        this.address = address;
    }

    public RoleEnum getRoleEnum() {
        return roleEnum;
    }

    public void setRoleEnum(RoleEnum roleEnum) {
        this.roleEnum = roleEnum;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
