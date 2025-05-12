package com.alex.post.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(name = "action-service")
public interface ActionClient {

    @GetMapping("api/v1/actions/followings/{user-id}")
    List<Integer> findUserFollowing(
            @PathVariable("user-id") Integer userId,
            @RequestHeader("Authorization") String token
    );
}
