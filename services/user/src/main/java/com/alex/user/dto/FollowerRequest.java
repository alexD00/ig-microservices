package com.alex.user.dto;

import java.util.Date;

public record FollowerRequest(
        Integer userId,
        Integer followerRequesterId,
        Date followerRequestSentAt
) {
}
