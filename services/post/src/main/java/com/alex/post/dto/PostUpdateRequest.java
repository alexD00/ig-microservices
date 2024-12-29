package com.alex.post.dto;

import jakarta.validation.constraints.NotNull;

public record PostUpdateRequest(

        @NotNull(message = "Content is required")
        String content
) {
}
