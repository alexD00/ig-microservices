package com.alex.post.dto;

import jakarta.validation.constraints.NotNull;

public record PostRequest(

        @NotNull(message = "Content is required")
        String content
) {
}
