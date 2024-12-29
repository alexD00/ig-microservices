package com.alex.post.service;

import com.alex.post.dto.PostRequest;
import com.alex.post.dto.PostResponse;
import com.alex.post.dto.PostUpdateRequest;
import jakarta.validation.Valid;

import java.util.List;

public interface PostService {

    PostResponse createPost(@Valid PostRequest postRequest);

    PostResponse updatePost(Integer postId, @Valid PostUpdateRequest postRequest);

    PostResponse findPostById(Integer postId);

    List<PostResponse> findAllPost();

    String deletePostById(Integer postId);
}
