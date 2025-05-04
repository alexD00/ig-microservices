package com.alex.action.dto;

import jakarta.validation.constraints.NotNull;

public record FollowerRequestDecision(

        @NotNull(message = "Decision is required")
        int isApprove
) {
}
