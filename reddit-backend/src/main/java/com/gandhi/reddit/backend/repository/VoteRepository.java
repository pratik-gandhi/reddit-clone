package com.gandhi.reddit.backend.repository;

import com.gandhi.reddit.backend.model.Post;
import com.gandhi.reddit.backend.model.User;
import com.gandhi.reddit.backend.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
    Optional<Vote> findTopByPostAndUserOrderByIdDesc(Post post, User currentUser);
}
