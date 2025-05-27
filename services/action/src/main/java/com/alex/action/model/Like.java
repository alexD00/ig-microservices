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
@IdClass(LikeId.class)
@Table(name = "likes")
public class Like {

    @Id
    private Integer userId;
    @Id
    private Integer postId;
    private LocalDateTime likedPostAt;
}
