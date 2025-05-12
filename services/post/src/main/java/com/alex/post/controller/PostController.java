package com.alex.post.controller;

import com.alex.post.dto.PostRequest;
import com.alex.post.dto.PostResponse;
import com.alex.post.dto.PostUpdateRequest;
import com.alex.post.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostResponse> createPost(
            @RequestBody @Valid PostRequest postRequest,
            @RequestHeader(value = "Authorization") String authToken,
            @RequestHeader(value = "X-User-Id") String userId
    ){
        return new ResponseEntity<>(postService.createPost(postRequest, authToken, userId), HttpStatus.CREATED);
    }

    @PutMapping("/{post-id}")
    public ResponseEntity<PostResponse> updatePost(
            @PathVariable("post-id") Integer postId,
            @RequestBody @Valid PostUpdateRequest postRequest,
            @RequestHeader(value = "X-User-Id") String userId
    ){
        return ResponseEntity.ok(postService.updatePost(postId, postRequest, userId));
    }

    @GetMapping("/{post-id}")
    public ResponseEntity<PostResponse> findPostById(
            @PathVariable("post-id") Integer postId
    ){
        return ResponseEntity.ok(postService.findPostById(postId));
    }

    @GetMapping
    public ResponseEntity<List<PostResponse>> findAllPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "desc") String direction
    ){
        return ResponseEntity.ok(postService.findAllPost(page, size, direction));
    }

    @GetMapping("/my-posts")
    public ResponseEntity<List<PostResponse>> findLoggedUserPosts(
            @PageableDefault(size = 10, page = 0) Pageable pageable,
            @RequestHeader(value = "X-User-Id") String userId

    ){
        return ResponseEntity.ok(postService.findLoggedUserPosts(pageable, userId));
    }

    @GetMapping("/user/{user-id}")
    public ResponseEntity<List<PostResponse>> findPostsByUserId(
            @PageableDefault(size = 10, page = 0) Pageable pageable,
            @RequestHeader(value = "Authorization") String authToken,
            @PathVariable("user-id") Integer userId
    ){
        return ResponseEntity.ok(postService.findPostsByUserId(pageable, authToken, userId));
    }

    @GetMapping("/my-feed")
    public ResponseEntity<List<PostResponse>> findLoggedUserFeed(
            @PageableDefault(size = 10, page = 0) Pageable pageable,
            @RequestHeader(value = "Authorization") String authToken,
            @RequestHeader(value = "X-User-Id") String userId
    ){
        return ResponseEntity.ok(postService.findLoggedUserFeed(pageable, authToken, userId));
    }

    @DeleteMapping("/{post-id}")
    public ResponseEntity<String> deletePostById(
            @PathVariable("post-id") Integer postId,
            @RequestHeader(value = "X-User-Id") String userId
    ){
        return ResponseEntity.ok(postService.deletePostById(postId, userId));
    }
}
