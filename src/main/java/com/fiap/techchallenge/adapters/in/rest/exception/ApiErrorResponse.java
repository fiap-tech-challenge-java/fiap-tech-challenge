package com.fiap.techchallenge.adapters.in.rest.exception;

import java.util.Map;

public class ApiErrorResponse {

    private String message;
    private String code;
    private int status;
    private String path;
    private Map<String, String> fieldErrors;

    public ApiErrorResponse(String message, String code, int status, String path) {
        this.message = message;
        this.code = code;
        this.status = status;
        this.path = path;
    }

    public void setFieldErrors(Map<String, String> fieldErrors) {
        this.fieldErrors = fieldErrors;
    }

    public String getMessage() {
        return message;
    }

    public String getCode() {
        return code;
    }

    public int getStatus() {
        return status;
    }

    public String getPath() {
        return path;
    }

    public Map<String, String> getFieldErrors() {
        return fieldErrors;
    }
}
