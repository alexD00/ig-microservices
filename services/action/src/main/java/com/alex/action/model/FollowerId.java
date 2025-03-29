package com.alex.action.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FollowerId implements Serializable { // Composite key for Follower model

    private Integer userId;
    private Integer followerId;
}
