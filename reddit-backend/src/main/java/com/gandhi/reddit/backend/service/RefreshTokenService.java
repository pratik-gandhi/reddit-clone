package com.gandhi.reddit.backend.service;

import com.gandhi.reddit.backend.model.RefreshToken;
import com.gandhi.reddit.backend.repository.RefreshTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken generateRefreshToken() {

        final var refreshToken = new RefreshToken();

        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setCreatedAt(Instant.now());

        return refreshTokenRepository.save(refreshToken);
    }

    public boolean isRefreshTokenValid(String token) {
        return refreshTokenRepository.findByToken(token).isPresent();
    }

    public void deleteRefreshTokenValid(String token) {
        refreshTokenRepository.deleteByToken(token);
    }
}
