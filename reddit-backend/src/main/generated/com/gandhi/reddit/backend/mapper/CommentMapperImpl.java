package com.gandhi.reddit.backend.mapper;

import com.gandhi.reddit.backend.dto.CommentsDto;
import com.gandhi.reddit.backend.model.Comment;
import com.gandhi.reddit.backend.model.Post;
import com.gandhi.reddit.backend.model.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-09-04T23:36:02+0200",
    comments = "version: 1.5.2.Final, compiler: javac, environment: Java 17.0.4.1 (Amazon.com Inc.)"
)
@Component
public class CommentMapperImpl implements CommentMapper {

    @Override
    public Comment map(CommentsDto commentsDto, Post post, User user) {
        if ( commentsDto == null && post == null && user == null ) {
            return null;
        }

        Comment comment = new Comment();

        if ( commentsDto != null ) {
            comment.setText( commentsDto.getText() );
        }
        comment.setPost( post );
        comment.setUser( user );
        comment.setCreatedAt( java.time.Instant.now() );

        return comment;
    }

    @Override
    public CommentsDto mapToDto(Comment comment) {
        if ( comment == null ) {
            return null;
        }

        CommentsDto commentsDto = new CommentsDto();

        commentsDto.setText( comment.getText() );

        commentsDto.setId( comment.getPost().getId() );
        commentsDto.setUsername( comment.getUser().getUsername() );

        return commentsDto;
    }
}
