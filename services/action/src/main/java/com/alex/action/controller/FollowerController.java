package com.alex.action.controller;

import com.alex.action.dto.FollowRequest;
import com.alex.action.service.FollowerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/actions/follow")
@RequiredArgsConstructor
public class FollowerController {

    private final FollowerService followerService;

    @PostMapping("/{userId-to-follow}")
    public ResponseEntity<String> followUnfollowUser(
            @RequestBody @Valid FollowRequest followRequest,
            @PathVariable("userId-to-follow") Integer userId,
            @RequestHeader(value = "Authorization") String authToken,
            @RequestHeader(value = "X-User-Id") String followerId
    ){
        return new ResponseEntity<>(followerService.followUnfollowUser(followRequest, userId, authToken, followerId), HttpStatus.CREATED);
    }
}
