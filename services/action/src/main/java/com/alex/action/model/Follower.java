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
@IdClass(FollowerId.class)
@Table(name = "followers")
public class Follower {

    @Id
    private Integer userId;
    @Id
    private Integer followerId;
    private LocalDateTime startedFollowingAt;
}
