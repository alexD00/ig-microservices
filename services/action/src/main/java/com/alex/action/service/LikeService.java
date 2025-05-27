package com.alex.action.service;

import com.alex.action.dto.LikeRequest;
import com.alex.action.dto.UserDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

public interface LikeService {

    String likeUnlikePost(LikeRequest likeRequest, Integer postId, String authToken, String userId);

    List<UserDto> findUserIdsWhoLikedPost(Integer postId, String authToken, String userId);
}
