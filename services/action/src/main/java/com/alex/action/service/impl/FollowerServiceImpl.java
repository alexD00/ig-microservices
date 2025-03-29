package com.alex.action.service.impl;

import com.alex.action.client.UserClient;
import com.alex.action.dto.FollowRequest;
import com.alex.action.exception.InvalidActionException;
import com.alex.action.model.Follower;
import com.alex.action.repository.FollowerRepository;
import com.alex.action.service.FollowerService;
import feign.FeignException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FollowerServiceImpl implements FollowerService {

    private final FollowerRepository followerRepository;
    private final UserClient userClient;

    @Override
    public String followUnfollowUser(FollowRequest followRequest, Integer userId, String authToken, String followerId) {
        try {
            userClient.findUserById(userId, authToken); // Check if user to follow exists
        } catch (FeignException.NotFound ex) {
            throw new EntityNotFoundException("User with id: " + userId + " was not found");
        }

        if (userId.equals(Integer.valueOf(followerId))){
            throw new InvalidActionException("User cannot follow their own account");
        }

        if (!followRequest.isFollow()){ // Unfollow user or no action
            if (followerRepository.findByUserIdAndFollowerId(userId, Integer.valueOf(followerId)).isPresent()){
                followerRepository.deleteFollowerByUserIdAndFollowerId(userId, Integer.valueOf(followerId));
                log.info("User with id: {} unfollowed user with id: {}", followerId, userId);
                return "User with id: " + followerId + " unfollowed user with id: " + userId;
            }

            log.info("No action, user with id: {} is not following user with id: {}", followerId, userId);
            return "No action, user with id: " + followerId + " is not following user with id: " + userId;
        }

        // Follow user or no action
        if (followerRepository.findByUserIdAndFollowerId(userId, Integer.valueOf(followerId)).isPresent()){
            return "No action, user with id: " + followerId + " already follows user with id: " + userId;
        }

        Follower follower = new Follower(userId, Integer.valueOf(followerId), LocalDateTime.now());

        followerRepository.save(follower);

        log.info("User with id: {} started following user with id: {}", followerId, userId);
        return "User with id: " + followerId + " started following user with id: " + userId;
    }

    @Override
    public List<Integer> findUserFollowers(Integer userId) {
        List<Integer> followersList = followerRepository.findFollowersIdByUserId(userId);

        return followersList;
    }

    @Override
    public List<Integer> findUserFollowing(Integer userId) {
        List<Integer> followingList = followerRepository.findFollowingsIdByUserId(userId);

        return followingList;
    }
}
