package com.alex.post.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record PostRequest(

        @NotNull(message = "Content is required")
        String content,
        @NotNull(message = "User id is required")
        @Positive(message = "User id must be a positive integer")
        Integer userId
) {
}
