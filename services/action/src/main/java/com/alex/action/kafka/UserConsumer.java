package com.alex.action.kafka;

import com.alex.action.model.FollowerRequest;
import com.alex.action.repository.FollowerRepository;
import com.alex.action.repository.FollowerRequestRepository;
import com.alex.action.service.impl.FollowerServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserConsumer {

    private final FollowerRequestRepository followerRequestRepository;
    private final FollowerRepository followerRepository;
    private final FollowerServiceImpl followerService;
    private static final String TOPIC = "user-deletion-topic";

    // When a user changes its account type from private to public all follower requests
    // will be automatically approved
    @KafkaListener(topics = "action-topic", groupId = "action-service")
    public void handleActionAsynchronously(String message){
        String [] parts = message.split("_");
        int userId = Integer.parseInt(parts[1]);

        if (message.startsWith("BATCHAPPROVE")){
            approveAllFollowerRequests(userId);
        } else if (message.startsWith("DELETEUSERACTIVITY")){
            deleteUserActivity(userId);
        } else {
            log.error("This Kafka message is not supported in the action topic");
        }
    }

    private void approveAllFollowerRequests(int userId){
        List<Integer> followerRequesterId = followerRequestRepository
                .findFollowerRequestsOfLoggedUser(userId)
                .stream()
                .map(FollowerRequest::getFollowerRequesterId)
                .toList();

        // Remove all follower requests sent to logged user since they are going to be accepted
        followerRequestRepository.deleteFollowerRequestByUserId(userId);

        for (int followerId: followerRequesterId){
            followerService.followUser(userId, followerId);
        }
    }

    private void deleteUserActivity(int userId){
        log.warn("Removing all follow requests sent or received by userId: {}...", userId);
        followerRequestRepository.deleteFollowerRequestByUserId(userId);
        followerRequestRepository.deleteFollowerRequestByFollowerRequesterId(userId);
        log.warn("Removed successfully all follow requests sent or received by userId: {}", userId);

        // Remove all followers for user to be deleted
        List<Integer> followerIdList = followerRepository.findFollowersIdByUserId(userId);
        for (int followerId: followerIdList){
            followerService.unfollowUser(userId, followerId);
        }
        log.warn("Removed successfully all followers of userId: {}", userId);

        List<Integer> followingIdList = followerRepository.findFollowingsIdByUserId(userId);
        for (int followingId: followingIdList){
            followerService.unfollowUser(followingId, userId);
        }
        log.warn("Removed successfully all followings of userId: {}", userId);
    }
}
