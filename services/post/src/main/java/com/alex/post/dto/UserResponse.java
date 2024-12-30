package com.alex.post.dto;

import java.io.Serializable;

public record UserResponse(

        Integer id,
        String firstName,
        String lastName,
        String email
) implements Serializable {
}
