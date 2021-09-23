package com.example.demo;

public class CustomError {
    private final String message;
    private final int status_code;

    CustomError(String message, int code) {
        this.message = message;
        this.status_code = code;
    }

    public String getMessage() {
        return message;
    }

    public int getStatus_code() {
        return status_code;
    }
}
