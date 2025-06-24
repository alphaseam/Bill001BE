package com.hotelapi.controller;

import com.hotelapi.dto.LoginForm;
import com.hotelapi.dto.LoginResponse;
import com.hotelapi.dto.RegisterForm;
import com.hotelapi.dto.RegisterResponse;
import com.hotelapi.service.JwtService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

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

    private final Map<String, String> registeredUsers = new ConcurrentHashMap<>();

    public LoginController(JwtService jwtService) {
        this.jwtService = jwtService;

        registeredUsers.put("test@example.com", "password");
    }

    @Operation(summary = "Register a new user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Registration successful"),
        @ApiResponse(responseCode = "400", description = "User already exists")
    })
    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterForm form) {
        if (registeredUsers.containsKey(form.getEmail())) {
            return ResponseEntity.badRequest().body(
                    new RegisterResponse(false, "User already exists"));
        }

        registeredUsers.put(form.getEmail(), form.getPassword());
        return ResponseEntity.ok(new RegisterResponse(true, "Registration successful"));
    }

    @Operation(summary = "Login and generate JWT token")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Login successful"),
        @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginForm loginForm) {
        String storedPassword = registeredUsers.get(loginForm.getEmail());

        if (storedPassword != null && storedPassword.equals(loginForm.getPassword())) {
            String accessToken = jwtService.generateAccessToken(loginForm.getEmail());
            String refreshToken = jwtService.generateRefreshToken(loginForm.getEmail());

            return ResponseEntity.ok(new LoginResponse(
                    true, "Login successful", accessToken, refreshToken));
        }

        return ResponseEntity.status(401).body(new LoginResponse(
                false, "Invalid email or password", null, null));
    }
}
