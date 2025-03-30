package com.alex.user.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

public record UserResponse(

        Integer id,
        String firstName,
        String lastName,
        String username,
        Integer numFollowers,
        Integer numFollowings,
        Boolean isAccountPublic,
        LocalDateTime createdAt
) implements Serializable {
}
