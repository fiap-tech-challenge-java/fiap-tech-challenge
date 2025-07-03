package com.fiap.techchallenge.application.ports.in.user.dtos;

import java.util.UUID;

public class ChangePassword {
    private UUID idUser;
    private String lastPassword;
    private String newPassword;

    public ChangePassword() {
    }

    public ChangePassword(UUID idUser, String lastPassword, String newPassword) {
        this.idUser = idUser;
        this.lastPassword = lastPassword;
        this.newPassword = newPassword;
    }

    public UUID getIdUser() {
        return idUser;
    }

    public void setIdUser(UUID idUser) {
        this.idUser = idUser;
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
}
