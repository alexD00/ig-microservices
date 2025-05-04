package com.alex.action.service.impl;

import com.alex.action.client.UserClient;
import com.alex.action.dto.FollowRequest;
import com.alex.action.dto.FollowerRequestDecision;
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
                return unfollowUser(userId, Integer.parseInt(followerId));
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

        return followUser(userId, Integer.parseInt(followerId));
    }

    @Override
    public List<FollowerRequest> findFollowerRequestsOfLoggedUser(String userId) {
        return followerRequestRepository
                .findFollowerRequestsOfLoggedUser(Integer.valueOf(userId));
    }

    @Override
    public String approveRejectFollowerRequest(FollowerRequestDecision decision, String userId, Integer followerId) {
        Optional<FollowerRequest> optionalFollowerRequest =
                followerRequestRepository
                        .findByUserIdAndFollowerRequesterId(Integer.valueOf(userId), followerId);

        // If follow request doesn't exist throw exception
        if (optionalFollowerRequest.isEmpty()){
            throw new InvalidActionException(
                    "UserId: " + userId + " does not have a follow request from userId: " + followerId
            );
        }

        if (decision.isApprove() == 1){
            followerRequestRepository
                    .deleteFollowerRequestByUserIdAndFollowerRequesterId(Integer.valueOf(userId), followerId);

            log.info("UserId: {} accepted follow request from userId: {}", userId, followerId);
            return followUser(Integer.parseInt(userId), followerId);
        } else if (decision.isApprove() == 0) {
            followerRequestRepository
                    .deleteFollowerRequestByUserIdAndFollowerRequesterId(Integer.valueOf(userId), followerId);

            log.info("UserId {} rejected follow request from userId: {}", userId, followerId);
            return "Rejected follow request from userId: " + followerId;
        }else {
            throw new RuntimeException("isApprove values must be either 1 or 0");
        }
    }

    public String followUser(int userId, int followerId){
        Follower follower = new Follower(userId, followerId, LocalDateTime.now());

        followerRepository.save(follower);

        int userData = followerRepository.countNumOfFollowers(userId);
        int followerData = followerRepository.countNumOfFollowings(followerId);
        String message = "FOLLOW_" + followerId + "_" + userId + "_" + followerData + "_" + userData;
        kafkaTemplate.send(TOPIC, message);

        log.info("User with id: {} started following user with id: {}", followerId, userId);
        return "User with id: " + followerId + " started following user with id: " + userId;
    }

    public String unfollowUser(int userId, int followerId){
        followerRepository.deleteFollowerByUserIdAndFollowerId(userId, followerId);

        int userData = followerRepository.countNumOfFollowers(userId);
        int followerData = followerRepository.countNumOfFollowings(followerId);
        String message = "UNFOLLOW_" + followerId + "_" + userId + "_" + followerData + "_" + userData;
        kafkaTemplate.send(TOPIC, message);

        log.info("User with id: {} unfollowed user with id: {}", followerId, userId);
        return "User with id: " + followerId + " unfollowed user with id: " + userId;
    }

    public String createFollowRequest(Integer userId, Integer followerId){
        if (followerRequestRepository.findByUserIdAndFollowerRequesterId(userId, followerId).isPresent()){
            return "No action, user already requested to follow userId: " + userId;
        }

        FollowerRequest followerRequest = new FollowerRequest(userId, followerId, LocalDateTime.now());
        followerRequestRepository.save(followerRequest);

        log.info("User with id: {} requested to follow user with id: {}", followerId, userId);
        return "Requested to follow userId: " + userId;
    }

    public String deleteFollowRequestIfExists(Integer userId, Integer followerId){
        Optional<FollowerRequest> followerRequest = followerRequestRepository
                .findByUserIdAndFollowerRequesterId(userId, followerId);

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
        return followerRepository.findFollowersIdByUserId(userId);
    }

    @Override
    public List<Integer> findUserFollowing(Integer userId) {
        return followerRepository.findFollowingsIdByUserId(userId);
    }
}
