package com.alex.action.controller;

import com.alex.action.dto.LikeRequest;
import com.alex.action.dto.UserDto;
import com.alex.action.service.LikeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/actions")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/like/{post-id}")
    public ResponseEntity<String> likeUnlikePost(
            @RequestBody @Valid LikeRequest likeRequest,
            @PathVariable("post-id") Integer postId,
            @RequestHeader(value = "Authorization") String authToken,
            @RequestHeader(value = "X-User-Id") String userId
    ){
        return new ResponseEntity<>(likeService.likeUnlikePost(likeRequest, postId, authToken, userId), HttpStatus.CREATED);
    }

    @GetMapping("/likes/posts/{post-id}")
    public ResponseEntity<List<UserDto>> findUsersWhoLikedPost(
         @PathVariable("post-id") Integer postId,
         @RequestHeader(value = "Authorization") String authToken,
         @RequestHeader(value = "X-User-Id") String userId
    ){
        return new ResponseEntity<>(likeService.findUserIdsWhoLikedPost(postId, authToken, userId), HttpStatus.OK);
    }
}
