package com.gandhi.reddit.backend.mapper;

import com.gandhi.reddit.backend.dto.PostDto;
import com.gandhi.reddit.backend.model.Post;
import com.gandhi.reddit.backend.model.Subreddit;
import com.gandhi.reddit.backend.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PostMapper {

    @Mapping(target = "id", source = "postDto.id")
    @Mapping(target = "postName", source = "postDto.name")
    @Mapping(target = "voteCount", source = "postDto.votes")
    @Mapping(target = "createdAt", expression = "java(java.time.Instant.now())")
    @Mapping(target = "user", source = "user")
    @Mapping(target = "subreddit", source = "subreddit")
    @Mapping(target = "description", source = "postDto.description")
    Post map(PostDto postDto, Subreddit subreddit, User user);

    @Mapping(target = "name", source = "postName")
    @Mapping(target = "votes", source = "voteCount")
    @Mapping(target = "subredditId", source = "post.subreddit.id")
    @Mapping(target = "subredditName", source = "post.subreddit.name")
    @Mapping(target = "username", source = "post.user.username")
    PostDto mapToDto(Post post);
}
