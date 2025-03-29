package com.alex.action.service;

import com.alex.action.dto.FollowRequest;

public interface FollowerService {

    String followUnfollowUser(FollowRequest followRequest, Integer userId, String authToken, String followerId);
}
