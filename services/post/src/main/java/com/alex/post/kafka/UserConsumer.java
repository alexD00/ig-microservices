package com.alex.post.kafka;

import com.alex.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserConsumer {

    private final PostRepository postRepository;

    @KafkaListener(topics = "post-topic", groupId = "post-service")
    public void handleDeletePostsAsynchronously(String message){
        String [] parts = message.split("_");
        int userId = Integer.parseInt(parts[1]);

        if (message.startsWith("DELETEUSERPOSTS")){
            deleteUserPosts(userId);
        }else {
            log.error("This Kafka message is not supported in post topic");
        }
    }

    private void deleteUserPosts(int userId){
        postRepository.deletePostsByUserId(userId);
        log.info("All posts of userId: {} were deleted successfully", userId);
    }
}
