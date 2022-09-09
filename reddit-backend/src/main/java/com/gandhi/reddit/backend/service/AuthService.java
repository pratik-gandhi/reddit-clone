package com.gandhi.reddit.backend.service;

import antlr.Token;
import com.gandhi.reddit.backend.dto.AuthenticationResponse;
import com.gandhi.reddit.backend.dto.LoginRequest;
import com.gandhi.reddit.backend.dto.RefreshTokenRequest;
import com.gandhi.reddit.backend.dto.RegisterRequest;
import com.gandhi.reddit.backend.exception.InvalidCredentialsException;
import com.gandhi.reddit.backend.exception.VerificationException;
import com.gandhi.reddit.backend.model.NotificationEmail;
import com.gandhi.reddit.backend.model.User;
import com.gandhi.reddit.backend.model.VerificationToken;
import com.gandhi.reddit.backend.repository.UserRepository;
import com.gandhi.reddit.backend.repository.VerificationTokenRepository;
import com.gandhi.reddit.backend.exception.EmailException;
import com.gandhi.reddit.backend.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;
    @Value("${app.signup.email.subject}")
    private final String signupEmailSubject;
    @Value("${app.signup.email.verification.base.url}")
    private final String verificationBaseUrl;
    private final AuthenticationManager authenticationManager;

    private final RefreshTokenService refreshTokenService;

    private final JwtProvider jwtProvider;

    @Transactional
    public void signup(final RegisterRequest registerRequest) throws EmailException {
        final var user = User.builder().username(registerRequest.getUsername()).email(registerRequest.getEmail()).password(passwordEncoder.encode(registerRequest.getPassword())).createdAt(Instant.now()).enabled(false).build();
        userRepository.save(user);

        final var verificationToken = generateVerificationToken(user);
        mailService.sendMail(NotificationEmail.builder().subject(signupEmailSubject)
                .recipient(user.getEmail())
                .body(
                        String.format("Dear %s,\n" +
                                "Thank you for signing up for Spring Reddit Clone.\n" +
                                "Please click on the link below to activate your account :" +
                                "%s/%s", user.getUsername(), verificationBaseUrl, verificationToken.getToken())
                )
                .build());
    }

    private VerificationToken generateVerificationToken(final User user) {
        final var verificationToken = VerificationToken.builder().token(UUID.randomUUID().toString()).user(user).expiryDate(Instant.now().plusSeconds(86400)).build();
        verificationTokenRepository.save(verificationToken);
        return verificationToken;
    }

    public void verifyAccount(final String token) {
        verificationTokenRepository.findByToken(token)
                .ifPresentOrElse(verificationToken -> {
                    final var user = verificationToken.getUser();
                    verificationTokenRepository.delete(verificationToken);
                    user.setEnabled(true);
                    userRepository.save(user);
                }, (() -> new VerificationException("Invalid token!")));
    }

    public AuthenticationResponse login(final LoginRequest loginRequest) {
        final var authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new InvalidCredentialsException(String.format("Invalid credentials %s", loginRequest.getUsername()));
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return new AuthenticationResponse(loginRequest.getUsername(),
                                            jwtProvider.generateToken(authentication),
                                            Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()),
                                            refreshTokenService.generateRefreshToken().getToken());
    }

    @Transactional(readOnly = true)
    public User getCurrentUser() {
        final var principal = (Jwt) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();
        return userRepository.findByUsername(principal.getSubject())
                .orElseThrow(() -> new UsernameNotFoundException("User name not found - " + principal.getSubject()));
    }

    public AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        final var refreshToken = refreshTokenRequest.getRefreshToken();
        if (!refreshTokenService.isRefreshTokenValid(refreshToken)) {
            throw new ResponseStatusException(HttpStatus.NOT_EXTENDED, "Refresh token is not valid");
        }

        // Delete the refresh token
        refreshTokenService.deleteRefreshTokenValid(refreshToken);

        // Generate new access & refresh token
        final var newToken = jwtProvider.generateToken(refreshTokenRequest.getUsername());
        final var newRefreshToken = refreshTokenService.generateRefreshToken();

        return AuthenticationResponse.builder()
                .jwt(newToken)
                .refreshToken(newRefreshToken.getToken())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
                .username(refreshTokenRequest.getUsername())
                .build();
    }
}
