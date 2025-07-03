package com.fiap.techchallenge.application.ports.in.user.dtos;

import java.util.UUID;

public class ChangePassword {
    private UUID idUser;
    private String newPassword;
    private String confirmPassword;

    public ChangePassword() {
    }

    public ChangePassword(UUID idUser, String newPassword) {
        this.idUser = idUser;
        this.newPassword = newPassword;
    }

    public UUID getIdUser() {
        return idUser;
    }

    public void setIdUser(UUID idUser) {
        this.idUser = idUser;
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
