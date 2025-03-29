package com.alex.user.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "action-service")
public interface ActionClient {

    @GetMapping("/api/v1/actions/followers/{user-id}")
    List<Integer> findFollowersIdOfUser(
            @PathVariable("user-id") Integer userId
    );

    @GetMapping("/api/v1/actions/followings/{user-id}")
    List<Integer> findFollowingsIdOfUser(
            @PathVariable("user-id") Integer userId
    );
}
