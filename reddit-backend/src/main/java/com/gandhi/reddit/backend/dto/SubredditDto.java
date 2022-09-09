package com.gandhi.reddit.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder(setterPrefix = "set")
@NoArgsConstructor
@AllArgsConstructor
public class SubredditDto {
    private Long id;
    private String name;
    private String description;
    private List<PostDto> posts;
    private int numberOfPosts;
}
