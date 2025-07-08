package com.fiap.techchallenge.domain.model;

public class HealthStatus {

    private String status;
    private String version;
    private String database;

    public HealthStatus(String status, String version, String database) {
        this.status = status;
        this.version = version;
        this.database = database;
    }

    public String getStatus() {
        return status;
    }

    public String getVersion() {
        return version;
    }

    public String getDatabase() {
        return database;
    }
}
