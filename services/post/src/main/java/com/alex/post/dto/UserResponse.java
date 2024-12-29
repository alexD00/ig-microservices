package com.alex.post.dto;

public record UserResponse(

        Integer id,
        String firstName,
        String lastName,
        String email
) {
}
