package com.alex.action.dto;

import jakarta.validation.constraints.NotNull;

public record FollowRequest(

        @NotNull(message = "Follow is required")
        Boolean isFollow
) {
}
