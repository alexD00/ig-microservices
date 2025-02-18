package com.alex.post.service;

import com.alex.post.dto.PostRequest;
import com.alex.post.dto.PostResponse;
import com.alex.post.dto.PostUpdateRequest;
import com.alex.post.exception.UserPermissionException;

import java.util.List;

public interface PostService {

    PostResponse createPost(PostRequest postRequest, String authToken, String userId);

    PostResponse updatePost(Integer postId, PostUpdateRequest postRequest, String userId);

    PostResponse findPostById(Integer postId);

    List<PostResponse> findAllPost();

    String deletePostById(Integer postId, String userId);
}
