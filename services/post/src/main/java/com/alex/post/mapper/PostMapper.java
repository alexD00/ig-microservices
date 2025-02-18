package com.alex.post.mapper;

import com.alex.post.dto.PostRequest;
import com.alex.post.dto.PostResponse;
import com.alex.post.model.Post;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PostMapper {

    public Post toPost(PostRequest postRequest, Integer userId){
        return Post.builder()
                .content(postRequest.content())
                .userId(userId)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public PostResponse toPostResponse(Post post){
        return new PostResponse(
                post.getId(),
                post.getContent(),
                post.getUserId(),
                post.getCreatedAt()
        );
    }
}
