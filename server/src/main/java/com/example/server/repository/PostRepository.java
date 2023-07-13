package com.example.server.repository;

import com.example.server.entity.Post;
import java.util.*;

import com.example.server.entity.User;
import jakarta.validation.OverridesAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {
    List<Post> findAllByUserOrderByCreateDateDesc(User user);
    List<Post> findAllByOrderByCreateDate();
    Optional<Post> findPostByIdAndUser(Long id,User user);
}
