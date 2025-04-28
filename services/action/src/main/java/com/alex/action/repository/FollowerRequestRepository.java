package com.alex.action.repository;

import com.alex.action.model.FollowerRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FollowerRequestRepository extends JpaRepository<FollowerRequest, Integer> {
    Optional<FollowerRequest> findByUserIdAndFollowerRequesterId(Integer userId, Integer followerId);
}
