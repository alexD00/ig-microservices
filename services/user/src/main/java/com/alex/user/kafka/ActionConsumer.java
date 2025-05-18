package com.alex.user.kafka;

import com.alex.user.model.User;
import com.alex.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ActionConsumer {

    private final UserRepository userRepository;

    @KafkaListener(topics = "user-action-topic", groupId = "user-service")
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
            log.error("This Kafka message is not supported for user-follow topic");
        }
    }

    private void updateFollowers(Integer userId, Integer userData) {
        // Using optional to get the user because when a user account is deleted the user is removed from the db
        // before the asynchronous call finishes. Using optional ensures that if the user was deleted it will be skipped
        // and the app won't fail with exceptions
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isPresent()){
            User user = optionalUser.get();
            user.setNumFollowers(userData);
            userRepository.save(user);
        }
        log.info("Updated num of followers of userId: {} successfully", userId);
    }

    private void updateFollowings(Integer userId, Integer userData) {
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isPresent()){
            User user = optionalUser.get();
            user.setNumFollowings(userData);
            userRepository.save(user);
        }
        log.info("Updated num of followings of userId: {} successfully", userId);
    }
}
