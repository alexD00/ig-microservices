package com.alex.action.service;

import com.alex.action.dto.LikeRequest;

public interface LikeService {

    String likeUnlikePost(LikeRequest likeRequest, Integer postId, String authToken, String userId);
}
