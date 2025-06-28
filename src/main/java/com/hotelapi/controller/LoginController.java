package com.hotelapi.controller;

import com.hotelapi.dto.LoginForm;
import com.hotelapi.dto.LoginResponse;
import com.hotelapi.dto.RegisterForm;
import com.hotelapi.dto.RegisterResponse;
import com.hotelapi.entity.User;
import com.hotelapi.repository.UserRepository;
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

    public LoginController(JwtService jwtService, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Operation(summary = "Register a new user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Registration successful"),
        @ApiResponse(responseCode = "400", description = "User already exists")
    })
    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterForm form) {
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
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginForm loginForm) {
        Optional<User> userOptional = userRepository.findByEmail(loginForm.getEmail());

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (passwordEncoder.matches(loginForm.getPassword(), user.getPassword())) {
                String accessToken = jwtService.generateAccessToken(user.getEmail());
                String refreshToken = jwtService.generateRefreshToken(user.getEmail());

                return ResponseEntity.ok(new LoginResponse(
                        true, "Login successful", accessToken, refreshToken));
            }
        }

        return ResponseEntity.status(401).body(new LoginResponse(
                false, "Invalid email or password", null, null));
    }
}
