package com.hotelapi.controller;

import com.hotelapi.dto.*;
import com.hotelapi.entity.User;
import com.hotelapi.repository.UserRepository;
import com.hotelapi.service.AuthService;
import com.hotelapi.service.JwtService;


import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
@Tag(name = "Authentication")
public class LoginController {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthService authService;

    public LoginController(JwtService jwtService,
                           UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           AuthService authService) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authService = authService;
    }

    @Operation(summary = "Register a new user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Registration successful"),
        @ApiResponse(responseCode = "400", description = "User already exists")
    })
    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest form) {
        if (userRepository.existsByEmail(form.getEmail())) {
            return ResponseEntity.badRequest().body(
                    new RegisterResponse(false, "User already exists"));
        }

        User user = new User();
        user.setEmail(form.getEmail());
        user.setPassword(passwordEncoder.encode(form.getPassword()));
        userRepository.save(user);

        return ResponseEntity.ok(new RegisterResponse(true, "Registration successful"));
    }

    @Operation(summary = "Login and generate JWT token")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Login successful"),
        @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        Optional<User> userOptional = userRepository.findByEmail(loginRequest.getEmail());

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                String accessToken = jwtService.generateAccessToken(user.getEmail());
                String refreshToken = jwtService.generateRefreshToken(user.getEmail());

                return ResponseEntity.ok(new LoginResponse(
                        true, "Login successful", accessToken, refreshToken));
            }
        }

        return ResponseEntity.status(401).body(new LoginResponse(
                false, "Invalid email or password", null, null));
    }

    @Operation(summary = "Send OTP to user's email")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "OTP sent successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid email or too many attempts")
    })
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        return ResponseEntity.ok(authService.sendOtp(request));
    }

    @Operation(summary = "Verify OTP for password reset")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "OTP verified"),
        @ApiResponse(responseCode = "400", description = "Invalid or expired OTP")
    })
    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@Valid @RequestBody VerifyOtpRequest request) {
        return ResponseEntity.ok(authService.verifyOtp(request));
    }

    @Operation(summary = "Reset password using OTP")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Password reset successful"),
        @ApiResponse(responseCode = "400", description = "OTP expired or invalid")
    })
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        return ResponseEntity.ok(authService.resetPassword(request));
    }
}

