package com.fiap.techchallenge.application.ports.in.user.dtos;

public class UpdateUser {
    private String name;
    private String login;
    private String email;

    public UpdateUser(String name, String login, String email) {
        this.name = name;
        this.login = login;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
