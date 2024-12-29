package com.alex.post.dto;

import java.time.LocalDateTime;

public record PostResponse(

        Integer id,
        String content,
        Integer userId,
        LocalDateTime createdAt
) {
}
