package com.alex.action.dto;

import jakarta.validation.constraints.NotNull;

public record LikeRequest(
        @NotNull(message = "Like is required")
        Boolean isLike
) {
}
