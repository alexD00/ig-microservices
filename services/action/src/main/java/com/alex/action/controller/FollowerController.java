package com.alex.action.controller;

import com.alex.action.dto.FollowRequest;
import com.alex.action.service.FollowerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/actions")
@RequiredArgsConstructor
public class FollowerController {

    private final FollowerService followerService;

    @PostMapping("/follow/{userId-to-follow}")
    public ResponseEntity<String> followUnfollowUser(
            @RequestBody @Valid FollowRequest followRequest,
            @PathVariable("userId-to-follow") Integer userId,
            @RequestHeader(value = "Authorization") String authToken,
            @RequestHeader(value = "X-User-Id") String followerId
    ){
        return new ResponseEntity<>(followerService.followUnfollowUser(followRequest, userId, authToken, followerId), HttpStatus.CREATED);
    }

    @GetMapping("/followers/{user-id}")
    public ResponseEntity<List<Integer>> findUserFollowers(
            @PathVariable("user-id") Integer userId
    ){
        return new ResponseEntity<>(followerService.findUserFollowers(userId), HttpStatus.OK);
    }

    @GetMapping("/followings/{user-id}")
    public ResponseEntity<List<Integer>> findUserFollowing( // Find accounts user is following
            @PathVariable("user-id") Integer userId
    ){
        return new ResponseEntity<>(followerService.findUserFollowing(userId), HttpStatus.OK);
    }
}
