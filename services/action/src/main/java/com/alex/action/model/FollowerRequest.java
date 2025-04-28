package com.alex.action.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@IdClass(FollowerRequestId.class)
@Table(name = "follower_requests")
public class FollowerRequest {

    @Id
    private Integer userId;
    @Id
    private Integer followerRequesterId;
    private LocalDateTime followerRequestSentAt;
}
