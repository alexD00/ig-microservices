package com.alex.post.client;

import com.alex.post.dto.UserResponse;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "user-service")
public interface UserClient {

    @GetMapping("api/v1/users/{user-id}")
//    @Cacheable(value = "users", key = "#userId")
    void findUserById(
            @PathVariable("user-id") Integer userId,
            @RequestHeader("Authorization") String token
    );
}
