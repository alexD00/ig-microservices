package com.alex.user.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

public record UserResponse(

        Integer id,
        String firstName,
        String lastName,
        String username,
        LocalDateTime createdAt
) implements Serializable {
}
