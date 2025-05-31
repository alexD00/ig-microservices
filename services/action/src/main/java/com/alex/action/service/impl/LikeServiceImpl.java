package com.alex.action.service.impl;

import com.alex.action.client.PostClient;
import com.alex.action.client.UserClient;
import com.alex.action.dto.LikeRequest;
import com.alex.action.exception.InvalidActionException;
import com.alex.action.model.Like;
import com.alex.action.repository.LikeRepository;
import com.alex.action.service.LikeService;
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
public class LikeServiceImpl implements LikeService {

    private final LikeRepository likeRepository;
    private final PostClient postClient;
    private final UserClient userClient;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private static final String POST_ACTION_TOPIC = "post-action-topic";

    @Override
    public String likeUnlikePost(LikeRequest likeRequest, Integer postId, String authToken, String userId) {
        checkPostExistsAndPermissions(authToken, userId, postId);

        Optional<Like> optionalLike = likeRepository.findByUserIdAndPostId(Integer.parseInt(userId), postId);
        if (likeRequest.isLike()){
            if (optionalLike.isEmpty()){
                Like like = new Like(Integer.parseInt(userId), postId, LocalDateTime.now());
                likeRepository.save(like);

                updateNumOfLikes(postId);

                log.info("UserId: {} liked postId: {}", userId, postId);
                return "UserId: " + userId + " liked postId: " + postId;
            }else {
                return "UserId: " + userId + " already liked postId: " + postId;
            }
        }else {
            if (optionalLike.isPresent()){
                likeRepository.deleteLikeByUserIdAndPostId(Integer.parseInt(userId), postId);

                updateNumOfLikes(postId);

                log.info("UserId: {} removed like from postId: {}", userId, postId);
                return "UserId: " + userId + " removed like from postId: " + postId;
            }else {
                return "No action taken, user hasn't liked postId: " + postId;
            }
        }
    }

    @Override
    public List<Integer> findUserIdsWhoLikedPost(Integer postId, String authToken, String userId) {
        checkPostExistsAndPermissions(authToken, userId, postId);

        return likeRepository.findUserIdsByPostId(postId);
    }

    private void updateNumOfLikes(int postId){
        Integer numOfLikes = likeRepository.findNumOfLikesOfPost(postId);
        String message = "UPDATENUMOFLIKES_" + postId + "_" + numOfLikes;
        kafkaTemplate.send(POST_ACTION_TOPIC, message);
    }

    private void checkPostExistsAndPermissions(String authToken, String loggedUserId, int postId){
        try {
            postClient.findPostById(authToken, loggedUserId, postId);
        }catch (FeignException.NotFound | FeignException.Forbidden ex){
            if (ex instanceof FeignException.NotFound){
                throw new EntityNotFoundException("Post with id " + postId + " was not found");
            }
            throw new InvalidActionException(
                    "User cannot view this post because post author " +
                            "has a private account and user doesn't follow them");
        }
    }
}
