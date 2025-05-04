package com.alex.action.repository;

import com.alex.action.model.FollowerRequest;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FollowerRequestRepository extends JpaRepository<FollowerRequest, Integer> {
    Optional<FollowerRequest> findByUserIdAndFollowerRequesterId(Integer userId, Integer followerId);

    @Query(
            value = "SELECT * FROM follower_requests WHERE user_id = ?1;",
            nativeQuery = true
    )
    List<FollowerRequest> findFollowerRequestsOfLoggedUser(Integer userId);

    @Transactional
    void deleteFollowerRequestByUserIdAndFollowerRequesterId(Integer userId, Integer followerId);

    @Transactional
    void deleteFollowerRequestByUserId(Integer userId);
}
