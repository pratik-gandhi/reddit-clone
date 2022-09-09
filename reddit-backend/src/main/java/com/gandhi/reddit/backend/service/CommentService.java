package com.gandhi.reddit.backend.service;
import com.gandhi.reddit.backend.dto.CommentsDto;
import com.gandhi.reddit.backend.exception.EmailException;
import com.gandhi.reddit.backend.exception.PostNotFoundException;
import com.gandhi.reddit.backend.exception.SpringRedditException;
import com.gandhi.reddit.backend.mapper.CommentMapper;
import com.gandhi.reddit.backend.model.NotificationEmail;
import com.gandhi.reddit.backend.model.User;
import com.gandhi.reddit.backend.repository.CommentRepository;
import com.gandhi.reddit.backend.repository.PostRepository;
import com.gandhi.reddit.backend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class CommentService {
    private static final String POST_URL = "";
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;
    private final MailContentBuilder mailContentBuilder;
    private final MailService mailService;

    public void save(CommentsDto commentsDto) {
        final var post = postRepository.findById(commentsDto.getPostId())
                .orElseThrow(() -> new PostNotFoundException(commentsDto.getPostId().toString()));
        final var comment = commentMapper.map(commentsDto, post, authService.getCurrentUser());
        commentRepository.save(comment);

        String message = mailContentBuilder.build(post.getUser().getUsername() + " posted a comment on your post." + POST_URL);
        sendCommentNotification(message, post.getUser());
    }

    private void sendCommentNotification(String message, User user) {
        mailService.sendMail(NotificationEmail
                                    .builder()
                                    .subject(user.getEmail())
                                    .subject(user.getUsername() + " Commented on your post")
                                    .body(message)
                                    .build());
    }

    public List<CommentsDto> getAllCommentsForPost(Long postId) {
        final var post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId.toString()));
        return commentRepository.findByPost(post)
                .stream()
                .map(commentMapper::mapToDto).collect(toList());
    }

    public List<CommentsDto> getAllCommentsForUser(String userName) {
        User user = userRepository.findByUsername(userName)
                .orElseThrow(() -> new UsernameNotFoundException(userName));
        return commentRepository.findAllByUser(user)
                .stream()
                .map(commentMapper::mapToDto)
                .collect(toList());
    }

    public boolean containsSwearWords(String comment) {
        if (comment.contains("shit")) {
            throw new SpringRedditException("Comments contains unacceptable language");
        }
        return false;
    }
}
