package com.gandhi.reddit.backend.mapper;

import com.gandhi.reddit.backend.dto.PostDto;
import com.gandhi.reddit.backend.dto.SubredditDto;
import com.gandhi.reddit.backend.model.Post;
import com.gandhi.reddit.backend.model.Subreddit;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-09-04T23:01:17+0200",
    comments = "version: 1.5.2.Final, compiler: javac, environment: Java 17.0.4.1 (Amazon.com Inc.)"
)
@Component
public class SubredditMapperImpl implements SubredditMapper {

    @Override
    public SubredditDto mapSubredditToDto(Subreddit subreddit) {
        if ( subreddit == null ) {
            return null;
        }

        SubredditDto.SubredditDtoBuilder subredditDto = SubredditDto.builder();

        subredditDto.setId( subreddit.getId() );
        subredditDto.setName( subreddit.getName() );
        subredditDto.setDescription( subreddit.getDescription() );
        subredditDto.setPosts( postListToPostDtoList( subreddit.getPosts() ) );

        subredditDto.setNumberOfPosts( mapPosts(subreddit.getPosts()) );

        return subredditDto.build();
    }

    @Override
    public Subreddit mapDtoToSubreddit(SubredditDto subredditDto) {
        if ( subredditDto == null ) {
            return null;
        }

        Subreddit.SubredditBuilder subreddit = Subreddit.builder();

        subreddit.setId( subredditDto.getId() );
        subreddit.setName( subredditDto.getName() );
        subreddit.setDescription( subredditDto.getDescription() );

        return subreddit.build();
    }

    protected PostDto postToPostDto(Post post) {
        if ( post == null ) {
            return null;
        }

        PostDto.PostDtoBuilder postDto = PostDto.builder();

        if ( post.getId() != null ) {
            postDto.id( post.getId() );
        }
        postDto.url( post.getUrl() );
        postDto.description( post.getDescription() );
        postDto.createdAt( post.getCreatedAt() );

        return postDto.build();
    }

    protected List<PostDto> postListToPostDtoList(List<Post> list) {
        if ( list == null ) {
            return null;
        }

        List<PostDto> list1 = new ArrayList<PostDto>( list.size() );
        for ( Post post : list ) {
            list1.add( postToPostDto( post ) );
        }

        return list1;
    }
}
