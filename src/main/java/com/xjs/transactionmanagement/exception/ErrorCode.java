package com.xjs.transactionmanagement.exception;

public enum ErrorCode {
    TRANSACTION_NOT_FOUND("Transaction not found"),
    TRANSACTION_ALREADY_EXISTS("Transaction name already exists"),
    INTERNAL_SERVER_ERROR("Internal Server Error");

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}