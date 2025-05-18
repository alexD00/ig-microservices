package com.alex.action.controller;

import com.alex.action.dto.FollowRequest;
import com.alex.action.dto.FollowerRequestDecision;
import com.alex.action.model.FollowerRequest;
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

    @GetMapping("follower-requests")
    public ResponseEntity<List<FollowerRequest>> findFollowerRequestsOfLoggedUser(
            @RequestHeader(value = "X-User-Id") String userId
    ){
        return new ResponseEntity<>(followerService.findFollowerRequestsOfLoggedUser(userId), HttpStatus.OK);
    }

    @PostMapping("follower-requests/{follower-id}")
    public ResponseEntity<String> approveRejectFollowerRequest(
            @RequestBody @Valid FollowerRequestDecision decision,
            @RequestHeader(value = "X-User-Id") String userId,
            @PathVariable(value = "follower-id") Integer followerId
            ){
        return new ResponseEntity<>(followerService.approveRejectFollowerRequest(decision, userId, followerId), HttpStatus.OK);
    }

    @DeleteMapping("/followers/{follower-id}/remove")
    public ResponseEntity<String> removeUserFollower(
            @RequestHeader(value = "X-User-Id") String userId,
            @RequestHeader(value = "Authorization") String authToken,
            @PathVariable(value = "follower-id") Integer followerId
            ){
        return new ResponseEntity<>(followerService.removeUserFollower(userId, authToken, followerId), HttpStatus.OK);
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
