package com.alex.action.service;

import com.alex.action.dto.LikeRequest;

import java.util.List;

public interface LikeService {

    String likeUnlikePost(LikeRequest likeRequest, Integer postId, String authToken, String userId);

    List<Integer> findUserIdsWhoLikedPost(Integer postId, String authToken, String userId);
}
