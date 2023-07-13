package com.example.server.repository;

import com.example.server.entity.Comment;
import com.example.server.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface CommentRepository extends JpaRepository<Comment, Long> {

  List<Comment> findAllByPost(Post post);
  Optional<Comment> findByIdAndUserId(Long id, Long userId);
}
