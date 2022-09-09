package com.gandhi.reddit.backend.repository;

import com.gandhi.reddit.backend.model.Comment;
import com.gandhi.reddit.backend.model.Post;
import com.gandhi.reddit.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPost(Post post);

    List<Comment> findAllByUser(User user);
}
