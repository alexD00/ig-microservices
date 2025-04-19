package com.alex.user.exception;

public class UserPermissionException extends RuntimeException {
    public UserPermissionException(String message) {
        super(message);
    }
}
