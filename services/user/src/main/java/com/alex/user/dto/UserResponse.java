package com.alex.user.dto;

import java.time.LocalDateTime;

public record UserResponse(

        Integer id,
        String firstName,
        String lastName,
        String email,
        LocalDateTime createdAt
) {
}
