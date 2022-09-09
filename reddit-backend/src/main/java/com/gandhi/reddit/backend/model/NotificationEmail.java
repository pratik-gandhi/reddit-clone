package com.gandhi.reddit.backend.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class NotificationEmail {
    private String subject;
    private String recipient;
    private String body;
}
