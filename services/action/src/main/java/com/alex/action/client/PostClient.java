package com.alex.action.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "post-service")
public interface PostClient {

    @GetMapping("api/v1/posts/{post-id}")
    void findPostById(
            @RequestHeader(value = "Authorization") String token,
            @RequestHeader(value = "X-User-Id") String loggedUserId,
            @PathVariable("post-id") Integer postId
    );
}
