package com.alex.post.controller;

import com.alex.post.dto.PostRequest;
import com.alex.post.dto.PostResponse;
import com.alex.post.dto.PostUpdateRequest;
import com.alex.post.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
            @RequestBody @Valid PostRequest postRequest
    ){
        return new ResponseEntity<>(postService.createPost(postRequest), HttpStatus.CREATED);
    }

    @PutMapping("/{post-id}")
    public ResponseEntity<PostResponse> updatePost(
            @PathVariable("post-id") Integer postId,
            @RequestBody @Valid PostUpdateRequest postRequest
    ){
        return ResponseEntity.ok(postService.updatePost(postId, postRequest));
    }

    @GetMapping("/{post-id}")
    public ResponseEntity<PostResponse> findPostById(
            @PathVariable("post-id") Integer postId
    ){
        return ResponseEntity.ok(postService.findPostById(postId));
    }

    @GetMapping
    public ResponseEntity<List<PostResponse>> findAllPosts(){
        return ResponseEntity.ok(postService.findAllPost());
    }

    @DeleteMapping("/{post-id}")
    public ResponseEntity<String> deletePostById(
            @PathVariable("post-id") Integer postId
    ){
        return ResponseEntity.ok(postService.deletePostById(postId));
    }
}
