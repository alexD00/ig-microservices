package com.alex.post.repository;

import com.alex.post.model.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer> {
    List<Post> findPostsByUserId(Pageable pageable, Integer userId);
}
