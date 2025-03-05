package com.market.controller;

import com.market.dto.AuthResponseDto;
import com.market.dto.RegisterRequest;
import com.market.dto.UserAuthDto;
import com.market.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authorization")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @Operation(summary = "User register", description = "Registers a new user with the provided information")
    public ResponseEntity<AuthResponseDto> register(@RequestBody @Valid RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/refresh-token")
    @Operation(summary = "Refresh token", description = "Obtains a new access token using a refresh token")
    public ResponseEntity<AuthResponseDto> refreshAccessToken(HttpServletRequest request) {
        return ResponseEntity.ok(authService.refreshAccessToken(request));
    }

    @PostMapping("/login")
    @Operation(summary = "User login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody @Valid UserAuthDto request, HttpServletResponse response) {
        return ResponseEntity.ok(authService.authenticate(request, response));
    }
}