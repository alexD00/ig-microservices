package com.alex.user.dto;

import jakarta.validation.constraints.*;

public record AuthRequest(

        @NotNull(message = "Email is required")
        @NotBlank(message = "Email cannot be blank")
        @Email(message = "Email must be valid")
        String username,
        @NotNull(message = "Password is required")
        @NotBlank(message = "Password cannot be blank")
        @Size(min = 8, message = "Password must be at least 8 characters long")
        @Pattern(regexp = "^[^\\s]{8,}$", message = "Password cannot contain any whitespace")
        String password
) {
}
