package com.alex.post.service.impl;

import com.alex.post.client.UserClient;
import com.alex.post.dto.PostRequest;
import com.alex.post.dto.PostResponse;
import com.alex.post.dto.PostUpdateRequest;
import com.alex.post.exception.UserPermissionException;
import com.alex.post.mapper.PostMapper;
import com.alex.post.model.Post;
import com.alex.post.repository.PostRepository;
import com.alex.post.service.PostService;
import feign.FeignException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final UserClient userClient;

    public PostResponse createPost(PostRequest postRequest, String authToken, String userId){
        try {
            userClient.findUserById(Integer.valueOf(userId), authToken);
        } catch (FeignException.NotFound ex) {
            throw new EntityNotFoundException("User with id: " + userId + " was not found");
        }
        Post post = postMapper.toPost(postRequest, Integer.valueOf(userId));

        postRepository.save(post);

        return postMapper.toPostResponse(post);
    }

    @Override
    public PostResponse updatePost(Integer postId, PostUpdateRequest postRequest, String userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post with id: " + postId + " was not found"));

        if (!post.getUserId().equals(Integer.valueOf(userId))){
                throw new UserPermissionException("User does not have permission to update this post");
        }

        post.setContent(postRequest.content());

        return postMapper.toPostResponse(postRepository.save(post));
    }

    public PostResponse findPostById(Integer postId){
        return postRepository.findById(postId)
                .map(postMapper::toPostResponse)
                .orElseThrow(() -> new EntityNotFoundException("Post with id: " + postId + " was not found"));
    }

    public List<PostResponse> findAllPost(){
        List<Post> postList = postRepository.findAll();
        List<PostResponse> postResponseList = new ArrayList<>();

        for (Post post: postList){
            postResponseList.add(postMapper.toPostResponse(post));
        }

        return postResponseList;
    }

    @Override
    public String deletePostById(Integer postId, String userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post with id: " + postId + " was not found"));

        if (!post.getUserId().equals(Integer.valueOf(userId))) {
            throw new UserPermissionException("User does not have permission to delete this post");
        }

        postRepository.deleteById(postId);

        return "Deleted post with id: " + postId;
    }
}
