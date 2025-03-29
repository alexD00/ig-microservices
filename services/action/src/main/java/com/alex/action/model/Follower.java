package com.alex.action.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "follower")
public class Follower {

    @Id
    private Integer userId;
    private Integer followerId;
    private LocalDateTime startedFollowingAt;
}
