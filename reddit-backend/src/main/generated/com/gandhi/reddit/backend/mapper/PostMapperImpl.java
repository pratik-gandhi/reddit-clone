package com.gandhi.reddit.backend.mapper;

import com.gandhi.reddit.backend.dto.PostDto;
import com.gandhi.reddit.backend.model.Post;
import com.gandhi.reddit.backend.model.Subreddit;
import com.gandhi.reddit.backend.model.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-09-04T23:36:02+0200",
    comments = "version: 1.5.2.Final, compiler: javac, environment: Java 17.0.4.1 (Amazon.com Inc.)"
)
@Component
public class PostMapperImpl implements PostMapper {

    @Override
    public Post map(PostDto postDto, Subreddit subreddit, User user) {
        if ( postDto == null && subreddit == null && user == null ) {
            return null;
        }

        Post.PostBuilder post = Post.builder();

        if ( postDto != null ) {
            post.id( postDto.getId() );
            post.postName( postDto.getName() );
            post.voteCount( postDto.getVotes() );
            post.description( postDto.getDescription() );
            post.url( postDto.getUrl() );
        }
        post.subreddit( subreddit );
        post.user( user );
        post.createdAt( java.time.Instant.now() );

        return post.build();
    }

    @Override
    public PostDto mapToDto(Post post) {
        if ( post == null ) {
            return null;
        }

        PostDto.PostDtoBuilder postDto = PostDto.builder();

        postDto.name( post.getPostName() );
        if ( post.getVoteCount() != null ) {
            postDto.votes( post.getVoteCount() );
        }
        Long id = postSubredditId( post );
        if ( id != null ) {
            postDto.subredditId( id );
        }
        postDto.subredditName( postSubredditName( post ) );
        postDto.username( postUserUsername( post ) );
        if ( post.getId() != null ) {
            postDto.id( post.getId() );
        }
        postDto.url( post.getUrl() );
        postDto.description( post.getDescription() );
        postDto.createdAt( post.getCreatedAt() );

        return postDto.build();
    }

    private Long postSubredditId(Post post) {
        if ( post == null ) {
            return null;
        }
        Subreddit subreddit = post.getSubreddit();
        if ( subreddit == null ) {
            return null;
        }
        Long id = subreddit.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String postSubredditName(Post post) {
        if ( post == null ) {
            return null;
        }
        Subreddit subreddit = post.getSubreddit();
        if ( subreddit == null ) {
            return null;
        }
        String name = subreddit.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }

    private String postUserUsername(Post post) {
        if ( post == null ) {
            return null;
        }
        User user = post.getUser();
        if ( user == null ) {
            return null;
        }
        String username = user.getUsername();
        if ( username == null ) {
            return null;
        }
        return username;
    }
}
