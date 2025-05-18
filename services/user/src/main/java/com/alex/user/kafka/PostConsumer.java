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
public class PostConsumer {

    private final UserRepository userRepository;

    @KafkaListener(topics = "user-post-topic", groupId = "user-service")
    public void handlePostEvents(String message){
        String[] parts = message.split("_");
        int userId = Integer.parseInt(parts[1]);
        int numOfPosts = Integer.parseInt(parts[2]);

        if (message.startsWith("UPDATENUMOFPOSTS")){
            updateNumOfPosts(userId, numOfPosts);
        }
    }

    private void updateNumOfPosts(int userId, int numOfPosts){
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isPresent()){
            User user = optionalUser.get();
            user.setNumOfPosts(numOfPosts);
            userRepository.save(user);
        }
        log.info("Updated num of posts of userId: {} successfully", userId);
    }
}
