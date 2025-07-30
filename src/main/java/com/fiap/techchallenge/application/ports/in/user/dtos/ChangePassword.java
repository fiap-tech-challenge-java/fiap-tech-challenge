package com.fiap.techchallenge.application.ports.in.user.dtos;

import java.util.UUID;

public class ChangePassword {
    private UUID id;
    private String lastPassword;
    private String newPassword;
    private String confirmPassword;

    public ChangePassword() {
    }

    public ChangePassword(UUID id, String newPassword) {
        this.id = id;
        this.newPassword = newPassword;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getLastPassword() {
        return lastPassword;
    }

    public void setLastPassword(String lastPassword) {
        this.lastPassword = lastPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
