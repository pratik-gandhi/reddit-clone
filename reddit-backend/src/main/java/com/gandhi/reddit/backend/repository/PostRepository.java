package com.gandhi.reddit.backend.repository;

import com.gandhi.reddit.backend.model.Post;
import com.gandhi.reddit.backend.model.Subreddit;
import com.gandhi.reddit.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllBySubreddit(Subreddit subreddit);

    List<Post> findAllByUser(User user);
}
