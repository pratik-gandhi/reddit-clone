package com.gandhi.reddit.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Jacksonized
public class PostDto {
    private long id;
    private String name;
    private String url;
    private String description;
    private int votes;
    @JsonProperty("created_at")
    private Instant createdAt;
    @JsonProperty("subreddit_id")
    private long subredditId;
    @JsonProperty("subreddit_name")
    private String subredditName;
    private String username;
}
