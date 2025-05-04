package com.alex.action.service;

import com.alex.action.dto.FollowRequest;
import com.alex.action.dto.FollowerRequestDecision;
import com.alex.action.model.FollowerRequest;

import java.util.List;

public interface FollowerService {

    String followUnfollowUser(FollowRequest followRequest, Integer userId, String authToken, String followerId);

    List<FollowerRequest> findFollowerRequestsOfLoggedUser(String userId);

    String approveRejectFollowerRequest(FollowerRequestDecision decision, String userId, Integer followerId);

    List<Integer> findUserFollowers(Integer userId);

    List<Integer> findUserFollowing(Integer userId);
}
