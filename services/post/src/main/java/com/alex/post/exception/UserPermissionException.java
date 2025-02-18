package com.alex.post.exception;

public class UserPermissionException extends RuntimeException {
    public UserPermissionException(String message) {
        super(message);
    }
}
