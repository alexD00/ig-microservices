package com.alex.action.repository;

import com.alex.action.model.Like;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Integer> {
    Optional<Like> findByUserIdAndPostId(Integer userId, Integer postId);

    @Transactional
    void deleteLikeByUserIdAndPostId(Integer userId, Integer postId);

    @Query(
            value = "SELECT COUNT(post_id) FROM likes WHERE post_id = ?1;",
            nativeQuery = true
    )
    Integer findNumOfLikesOfPost(Integer postId);
}
