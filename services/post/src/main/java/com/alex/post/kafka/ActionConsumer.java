package com.alex.post.kafka;

import com.alex.post.model.Post;
import com.alex.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ActionConsumer {

    private final PostRepository postRepository;

    @KafkaListener(topics = "post-action-topic", groupId = "post-service")
    public void handleDeletePostsAsynchronously(String message){
        String [] parts = message.split("_");
        int postId = Integer.parseInt(parts[1]);
        int numOfLikes = Integer.parseInt(parts[2]);

        if (message.startsWith("UPDATENUMOFLIKES_")){
            updatePostLikes(postId, numOfLikes);
        }else {
            log.error("This Kafka message is not supported in post topic");
        }
    }

    private void updatePostLikes(int postId, int numOfLikes){
        Optional<Post> optionalPost = postRepository.findById(postId);

        if (optionalPost.isPresent()){
            Post post = optionalPost.get();
            post.setNumOfLikes(numOfLikes);
            postRepository.save(post);
            log.info("Number of likes of postId: {} were updated to {}", postId, numOfLikes);
        }
    }
}
