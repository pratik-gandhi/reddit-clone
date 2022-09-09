package com.gandhi.reddit.backend.service;

import com.gandhi.reddit.backend.dto.PostDto;
import com.gandhi.reddit.backend.dto.SubredditDto;
import com.gandhi.reddit.backend.mapper.SubredditMapper;
import com.gandhi.reddit.backend.model.Post;
import com.gandhi.reddit.backend.model.Subreddit;
import com.gandhi.reddit.backend.repository.SubredditRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubredditService {

    private final SubredditRepository subredditRepository;
    private final SubredditMapper subredditMapper;


    @Transactional
    public SubredditDto create(SubredditDto subredditDto) {

        final var subreddit = subredditMapper.mapDtoToSubreddit(subredditDto);

//        final var subreddit = Subreddit.builder().setName(subredditDto.getName())
//                .setDescription(subredditDto.getDescription())
//                .setCreatedAt(Instant.now())
//                .build();

        final var savedSubreddit = subredditRepository.save(subreddit);

        subredditDto.setId(savedSubreddit.getId());

        return subredditDto;
    }

    @Transactional(readOnly = true, isolation = Isolation.REPEATABLE_READ)
    public SubredditDto getSubreddit(long id) {
        return subredditRepository.findById(id)
                .map(subredditMapper::mapSubredditToDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("No Subreddit with id %s", id)));
    }


    @Transactional(readOnly = true, isolation = Isolation.REPEATABLE_READ)
    public List<SubredditDto> getAllSubreddit() {
        return subredditRepository.findAll()
                .stream()
                .map(subredditMapper::mapSubredditToDto)
                .collect(Collectors.toList());
    }


    @Transactional
    public void delete(long id) {
        subredditRepository.findById(id)
                .ifPresentOrElse(subreddit -> subredditRepository.delete(subreddit), () -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("No subreddit with id %s", id)));
    }


    public SubredditDto update(long id, SubredditDto subredditDto) {
        return subredditRepository.findById(id)
                .map(subreddit -> {
                    subreddit.setName(subredditDto.getName());
                    subreddit.setDescription(subredditDto.getDescription());
                    final var savedSubreddit = subredditRepository.save(subreddit);
                    return savedSubreddit;
                }).map(subredditMapper::mapSubredditToDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Subreddit with id %s not found", id)));
    }

    public SubredditDto patch(long id, SubredditDto subredditDto) {
        return subredditRepository.findById(id)
                .map(subreddit -> {

                    if (subredditDto.getName() != null)
                        subreddit.setName(subredditDto.getName());

                    if (subreddit.getDescription() != null)
                        subreddit.setDescription(subredditDto.getDescription());

                    final var savedSubreddit = subredditRepository.save(subreddit);
                    return savedSubreddit;
                }).map(subredditMapper::mapSubredditToDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Subreddit with id %s not found", id)));
    }

    private SubredditDto toSubredditDto(Subreddit subreddit) {
        return SubredditDto.builder()
                .setId(subreddit.getId())
                .setName(subreddit.getName())
                .setDescription(subreddit.getDescription())
                .setPosts(subreddit.getPosts().stream().map(post -> this.toPostDto(post, subreddit.getId())).collect(Collectors.toList()))
                .build();
    }

    private PostDto toPostDto(Post post, long subredditId) {
        return PostDto.builder()
                .id(post.getId())
                .createdAt(post.getCreatedAt())
                .name(post.getPostName())
                .description(post.getDescription())
                .url(post.getUrl())
                .votes(post.getVoteCount())
                .subredditId(subredditId)
                .build();
    }
}
