package com.alex.user.kafka;

import com.alex.user.model.User;
import com.alex.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ActionConsumer {

    private final UserRepository userRepository;

    @KafkaListener(topics = "user-follow-topic", groupId = "user-service")
    public void handleFollowUnfollowEvents(String message){
        String[] parts = message.split("_");
        Integer followerId = Integer.parseInt(parts[1]);
        Integer userId = Integer.parseInt(parts[2]);
        Integer followerData = Integer.parseInt(parts[3]);
        Integer userData = Integer.parseInt(parts[4]);

        if (message.startsWith("FOLLOW") || message.startsWith("UNFOLLOW")){
            updateFollowers(userId, userData);
            updateFollowings(followerId, followerData);
        }else {
            log.error("This Kafka message is not supported for user topic");
        }
    }

    private void updateFollowers(Integer userId, Integer userData) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        user.setNumFollowers(userData);
        userRepository.save(user);
    }

    private void updateFollowings(Integer userId, Integer userData) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        user.setNumFollowings(userData);
        userRepository.save(user);
    }
}
