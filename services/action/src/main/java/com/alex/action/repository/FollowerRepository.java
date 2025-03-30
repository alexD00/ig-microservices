package com.alex.action.repository;

import com.alex.action.model.Follower;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowerRepository extends JpaRepository<Follower, Integer> {
    Optional<Follower> findByUserIdAndFollowerId(Integer userId, Integer followerId);

    @Transactional
    void deleteFollowerByUserIdAndFollowerId(Integer userId, Integer followerId);

    @Query(
            value = "SELECT follower_id FROM followers WHERE user_id = ?1;",
            nativeQuery = true
    )
    List<Integer> findFollowersIdByUserId(Integer userId);

    @Query(
            value = "SELECT user_id FROM followers WHERE follower_id = ?1;",
            nativeQuery = true
    )
    List<Integer> findFollowingsIdByUserId(Integer userId);
}
