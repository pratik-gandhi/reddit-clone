package com.gandhi.reddit.backend.controller.v1;

import com.gandhi.reddit.backend.dto.AuthenticationResponse;
import com.gandhi.reddit.backend.dto.LoginRequest;
import com.gandhi.reddit.backend.dto.RefreshTokenRequest;
import com.gandhi.reddit.backend.dto.RegisterRequest;
import com.gandhi.reddit.backend.exception.EmailException;
import com.gandhi.reddit.backend.service.RefreshTokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.gandhi.reddit.backend.service.AuthService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;

    @Operation(summary = "Signup to create a new account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Signup successful")
    })
    @PostMapping("/signup")
    public ResponseEntity<String> signup (@RequestBody RegisterRequest registerRequest) throws EmailException {
        authService.signup(registerRequest);
        return ResponseEntity.ok("User registration complete");
    }

    @Operation(summary = "Activate user account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Signup successful"),
            @ApiResponse(responseCode = "400", description = "Invalid/Non-existent token")
    })
    @GetMapping("/verification/{token}")
    public ResponseEntity<String> verify(@PathVariable("token") String token) {
        authService.verifyAccount(token);
        return ResponseEntity.ok("User activated successfully");
    }

    @Operation(summary = "Login")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = AuthenticationResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid Credentials")
    })
    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }

    @PostMapping("/refresh/token")
    public AuthenticationResponse refreshToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        return authService.refreshToken(refreshTokenRequest);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        refreshTokenService.deleteRefreshTokenValid(refreshTokenRequest.getRefreshToken());
        return ResponseEntity.status(HttpStatus.OK).body("Refresh token deleted successfully");
    }
}
