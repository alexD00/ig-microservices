package com.alex.action.kafka;

import com.alex.action.model.FollowerRequest;
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
    private final FollowerServiceImpl followerService;

    // When a user changes its account type from private to public all follower requests
    // will be automatically approved
    @KafkaListener(topics = "action-batchapprove-follower-requests-topic", groupId = "action-service")
    public void handleBatchApproveFollowerRequest(String message){
        String [] parts = message.split("_");
        int userId = Integer.parseInt(parts[1]);

        if (message.startsWith("BATCHAPPROVE")){
            approveAllFollowerRequests(userId);
        }else {
            log.error("This Kafka message is not supported for user topic");
        }

    }

    public void approveAllFollowerRequests(int userId){
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

}
