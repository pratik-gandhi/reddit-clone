package com.gandhi.reddit.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Jacksonized
public class CommentsDto {
    private Long id;
    @JsonProperty("post_id")
    private Long postId;
    @JsonProperty("created_at")
    private Instant createdDate;
    private String text;
    private String username;
}