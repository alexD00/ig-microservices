package com.alex.action.service.impl;

import com.alex.action.client.UserClient;
import com.alex.action.dto.FollowRequest;
import com.alex.action.dto.UserDto;
import com.alex.action.exception.InvalidActionException;
import com.alex.action.model.Follower;
import com.alex.action.model.FollowerRequest;
import com.alex.action.repository.FollowerRequestRepository;
import com.alex.action.repository.FollowerRepository;
import com.alex.action.service.FollowerService;
import feign.FeignException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FollowerServiceImpl implements FollowerService {

    private final FollowerRepository followerRepository;
    private final FollowerRequestRepository followerRequestRepository;
    private final UserClient userClient;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private static final String TOPIC = "user-follow-topic";

    @Override
    public String followUnfollowUser(FollowRequest followRequest, Integer userId, String authToken, String followerId) {
        UserDto userToFollow;
        try {
            userToFollow = userClient.findUserById(userId, authToken); // Check if user to follow exists and get the account type
        } catch (FeignException.NotFound ex) {
            throw new EntityNotFoundException("User with id: " + userId + " was not found");
        }

        if (userId.equals(Integer.valueOf(followerId))){
            throw new InvalidActionException("User cannot follow their own account");
        }

        // Unfollow user or remove follow request or no action
        if (!followRequest.isFollow()){
            if (followerRepository.findByUserIdAndFollowerId(userId, Integer.valueOf(followerId)).isPresent()){
                followerRepository.deleteFollowerByUserIdAndFollowerId(userId, Integer.valueOf(followerId));
                log.info("User with id: {} unfollowed user with id: {}", followerId, userId);

                String message = "UNFOLLOW_" + followerId + "_" + userId;
                kafkaTemplate.send(TOPIC, message);

                return "User with id: " + followerId + " unfollowed user with id: " + userId;
            }

            return deleteFollowRequestIfExists(userId, Integer.valueOf(followerId));
        }

        // Follow user or create follow request or no action
        if (followerRepository.findByUserIdAndFollowerId(userId, Integer.valueOf(followerId)).isPresent()){
            return "No action, user with id: " + followerId + " already follows user with id: " + userId;
        }

        // If user to follow has private account then create a follow request
        if (!userToFollow.isAccountPublic()){
            return createFollowRequest(userId, Integer.valueOf(followerId));
        }

        Follower follower = new Follower(userId, Integer.valueOf(followerId), LocalDateTime.now());

        followerRepository.save(follower);

        String message = "FOLLOW_" + followerId + "_" + userId;
        kafkaTemplate.send(TOPIC, message);

        log.info("User with id: {} started following user with id: {}", followerId, userId);
        return "User with id: " + followerId + " started following user with id: " + userId;
    }

    public String createFollowRequest(Integer userId, Integer followerId){
        if (followerRequestRepository.findByUserIdAndFollowerRequesterId(userId, followerId).isPresent()){
            return "No action, user already requested to follow userId: " + userId;
        }

        FollowerRequest followerRequest = new FollowerRequest(userId, followerId, LocalDateTime.now());
        followerRequestRepository.save(followerRequest);

        return "Requested to follow userId: " + userId;
    }

    public String deleteFollowRequestIfExists(Integer userId, Integer followerId){
        Optional<FollowerRequest> followerRequest = followerRequestRepository.findByUserIdAndFollowerRequesterId(userId, followerId);

        if (followerRequest.isPresent()){ // If a follow request exists remove it
            followerRequestRepository.delete(followerRequest.get());
            log.warn("User removed follow request sent to userId: {}", userId);
            return "User removed follow request sent to userId: " + userId;
        }

        // No action, if not following or no follow request created
        log.info("No action, user with id: {} is not following user with id: {}", followerId, userId);
        return "No action, user with id: " + followerId + " is not following user with id: " + userId;
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
