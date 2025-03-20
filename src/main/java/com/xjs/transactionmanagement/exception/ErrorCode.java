package com.xjs.transactionmanagement.exception;

public enum ErrorType {
    RESOURCE_NOT_FOUND("E001", "Resource not found"),
    INVALID_INPUT("E002", "Invalid input provided"),
    DATABASE_ERROR("E003", "Database error occurred"),
    UNAUTHORIZED_ACCESS("E004", "Unauthorized access"),
    INTERNAL_SERVER_ERROR("E005", "An unexpected error occurred");

    private final String code;
    private final String message;

    ErrorType(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}