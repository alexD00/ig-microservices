package com.alex.action.service;

import com.alex.action.dto.FollowRequest;

import java.util.List;

public interface FollowerService {

    String followUnfollowUser(FollowRequest followRequest, Integer userId, String authToken, String followerId);

    List<Integer> findUserFollowers(Integer userId);

    List<Integer> findUserFollowing(Integer userId);
}
