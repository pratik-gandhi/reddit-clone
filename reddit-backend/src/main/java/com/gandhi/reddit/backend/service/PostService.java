package com.gandhi.reddit.backend.service;

import com.gandhi.reddit.backend.dto.PostDto;
import com.gandhi.reddit.backend.mapper.PostMapper;
import com.gandhi.reddit.backend.repository.PostRepository;
import com.gandhi.reddit.backend.repository.SubredditRepository;
import com.gandhi.reddit.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final SubredditRepository subredditRepository;
    private final AuthService authService;
    private final PostMapper postMapper;
    private final UserRepository userRepository;

    @Transactional
    public PostDto createPost(final PostDto postDto) {
        final var subReddit = subredditRepository.findById(postDto.getSubredditId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("No subreddit with id %s found!", postDto.getSubredditId())));

        final var user = authService.getCurrentUser();

        final var post = postMapper.map(postDto, subReddit, user);

        final var savedPost = postRepository.save(post);

        postDto.setId(savedPost.getId());

        return postDto;
    }

    @Transactional(readOnly = true)
    public PostDto getPost(final long id) {
        return postRepository.findById(id).map(postMapper::mapToDto).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Post with id %s not found", id)));
    }

    @Transactional(readOnly = true)
    public List<PostDto> getAllPost() {
        return postRepository.findAll().stream().map(postMapper::mapToDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PostDto> getPostsBySubreddit(final long subredditId) {
        final var subreddit = subredditRepository.findById(subredditId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("No subreddit with id %s found!", subredditId)));

        return postRepository.findAllBySubreddit(subreddit).stream().map(postMapper::mapToDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PostDto> getPostsByUsername(final String username) {
        final var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("No User found by username %s", username)));

        return postRepository.findAllByUser(user).stream().map(postMapper::mapToDto).collect(Collectors.toList());
    }
}
