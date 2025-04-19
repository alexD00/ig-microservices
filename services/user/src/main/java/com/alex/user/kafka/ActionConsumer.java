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

        if (message.startsWith("FOLLOW")){
            incrementFollowers(userId);
            incrementFollowings(followerId);
        } else if (message.startsWith("UNFOLLOW")) {
            decreaseFollowers(userId);
            decreaseFollowings(followerId);
        }else {
            log.error("This Kafka message is not supported for user topic");
        }
    }

    private void incrementFollowers(Integer userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        user.setNumFollowers(user.getNumFollowers() + 1);
        userRepository.save(user);
    }

    private void incrementFollowings(Integer userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        user.setNumFollowings(user.getNumFollowings() + 1);
        userRepository.save(user);
    }

    private void decreaseFollowers(Integer userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        user.setNumFollowers(user.getNumFollowers() - 1);
        userRepository.save(user);
    }

    private void decreaseFollowings(Integer userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        user.setNumFollowings(user.getNumFollowings() - 1);
        userRepository.save(user);
    }
}
