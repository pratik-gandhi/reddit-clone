package com.gandhi.reddit.backend.dto;

import lombok.Builder;

import java.time.Instant;

@Builder
public record AuthenticationResponse(String username, String jwt, Instant expiresAt, String refreshToken) {}
