package com.market.controller;

import com.market.dto.AuthRequest;
import com.market.dto.AuthResponse;
import com.market.dto.AuthResponseDto;
import com.market.dto.RegisterRequest;
import com.market.dto.UserAuthDto;
import com.market.entity.User;
import com.market.enumeration.Role;
import com.market.service.AuthService;
import com.market.service.UserService;
import com.market.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authorization")
public class AuthController {

    private final UserService userService;
    private final AuthService authService;
    private final JwtUtil jwtUtil;

//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
//        // Тут можна через AuthenticationManagerBuilder спробувати аутентифікувати
//        // або напряму перевірити користувача
//        Optional<User> userOpt = userService.findByUsername(request.username());
//        if (userOpt.isPresent()) {
//            User user = userOpt.get();
//            // перевірка пароля
//            if (new BCryptPasswordEncoder().matches(request.password(), user.getPassword())) {
//                String token = jwtUtil.generateToken(user.getUsername(), user.getRole());
//                return ResponseEntity.ok(new AuthResponse(token));
//            }
//        }
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
//    }
//
//    @PostMapping("/register")
//    @Operation(summary = "User register", description = "Registers a new user with the provided information")
//    public ResponseEntity<?> register(@RequestBody AuthRequest request) {
//        // Реєструємо з роллю USER
//        User user = userService.register(request.email(), request.username(), request.password(), Role.USER);
//        String token = jwtUtil.generateToken(user.getUsername(), user.getRole());
//        return ResponseEntity.ok(new AuthResponse(token));
//    }

//    @PostMapping("/refresh-token")
//    @Operation(summary = "Refresh token", description = "Obtains a new access token using a refresh token")
//    public ResponseEntity<AuthResponseDto> refreshAccessToken(HttpServletRequest request,
//                                                              HttpServletResponse response) {
//        return ResponseEntity.ok(authService.refreshAccessToken(request, response));
//    }
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