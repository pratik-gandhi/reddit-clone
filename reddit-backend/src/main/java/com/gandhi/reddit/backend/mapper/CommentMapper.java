package com.gandhi.reddit.backend.mapper;

import com.gandhi.reddit.backend.dto.CommentsDto;
import com.gandhi.reddit.backend.model.Comment;
import com.gandhi.reddit.backend.model.Post;
import com.gandhi.reddit.backend.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "text", source = "commentsDto.text")
    // TODO: Should we always generate new created_at?
    @Mapping(target = "createdAt", expression = "java(java.time.Instant.now())")
    @Mapping(target = "post", source = "post")
    @Mapping(target = "user", source = "user")
    Comment map(CommentsDto commentsDto, Post post, User user);

    @Mapping(target = "id", expression = "java(comment.getPost().getId())")
    @Mapping(target = "username", expression = "java(comment.getUser().getUsername())")
    CommentsDto mapToDto(Comment comment);
}
