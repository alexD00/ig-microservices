package com.alex.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserRequest(

        @NotNull(message = "First name is required")
        String firstName,
        @NotNull(message = "Last name is required")
        String lastName,
        @NotNull(message = "Email is required")
        @Email(message = "Email must be valid")
        String email,
        @NotNull(message = "Password is required")
        @Size(min = 8, message = "Password must be at least 8 characters long")
        String password
) {
}
