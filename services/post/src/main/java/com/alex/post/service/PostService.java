package com.alex.post.service;

import com.alex.post.dto.PostRequest;
import com.alex.post.dto.PostResponse;
import com.alex.post.dto.PostUpdateRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostService {

    PostResponse createPost(PostRequest postRequest, String authToken, String userId);

    PostResponse updatePost(Integer postId, PostUpdateRequest postRequest, String userId);

    PostResponse findPostById(String authToken, String loggedUserId, Integer postId);

    List<PostResponse> findAllPost(int page, int size, String direction);

    List<PostResponse> findLoggedUserPosts(Pageable pageable, String userId);

    List<PostResponse> findPostsByUserId(Pageable pageable, String authToken, String loggedUserId, Integer userId);

    List<PostResponse> findLoggedUserFeed(Pageable pageable, String authToken, String userId);

    String deletePostById(Integer postId, String userId);
}
