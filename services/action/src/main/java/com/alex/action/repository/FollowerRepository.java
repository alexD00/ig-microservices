package com.alex.action.repository;

import com.alex.action.model.Follower;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FollowerRepository extends JpaRepository<Follower, Integer> {
    Optional<Follower> findByUserIdAndFollowerId(Integer userId, Integer followerId);

    @Transactional
    void deleteFollowerByUserIdAndFollowerId(Integer userId, Integer followerId);
}
