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
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
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
        log.info("Post with id: {} created successfully by user with id: {}", post.getId(), userId);

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
        log.info("Post with id: {} was updated successfully", post.getId());

        return postMapper.toPostResponse(postRepository.save(post));
    }

    public PostResponse findPostById(Integer postId){
        return postRepository.findById(postId)
                .map(postMapper::toPostResponse)
                .orElseThrow(() -> new EntityNotFoundException("Post with id: " + postId + " was not found"));
    }

    public List<PostResponse> findAllPost(int page, int size, String direction){
        Sort.Direction sortDirection = direction.equalsIgnoreCase("asc")
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(sortDirection, "createdAt"));

        List<Post> postList = postRepository.findAll(pageRequest).getContent();
        List<PostResponse> postResponseList = new ArrayList<>();

        for (Post post: postList){
            postResponseList.add(postMapper.toPostResponse(post));
        }

        return postResponseList;
    }

    @Override
    public List<PostResponse> findLoggedUserPosts(Pageable pageable, String userId) {
        List<Post> postList = postRepository.findPostsByUserId(pageable, Integer.valueOf(userId));
        List<PostResponse> postResponseList = new ArrayList<>();

        for (Post post: postList){
            postResponseList.add(postMapper.toPostResponse(post));
        }

        return postResponseList;
    }

    @Override
    public List<PostResponse> findPostsByUserId(Pageable pageable, String authToken, Integer userId) {
        try {
            userClient.findUserById(userId, authToken);
        } catch (FeignException.NotFound ex) {
            throw new EntityNotFoundException("User with id: " + userId + " was not found");
        }

        List<Post> postList = postRepository.findPostsByUserId(pageable, userId);
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
        log.warn("Post with id: {} was deleted successfully", post.getId());

        return "Deleted post with id: " + postId;
    }
}
