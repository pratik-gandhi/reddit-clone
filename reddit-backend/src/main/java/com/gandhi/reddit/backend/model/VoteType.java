package com.gandhi.reddit.backend.model;

import java.util.Arrays;

public enum VoteType {

    UPVOTE(1),
    DOWNVOTE(-1);

    private int score;

    VoteType(int score) {
        this.score = score;
    }

    public static VoteType fromScore(Integer score) {
        return Arrays.stream(values())
                .filter(value -> value.score == score)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format("No matching vote type found for given score: %s", score)));
    }
}
