package com.hotelapi.controller;

import com.hotelapi.dto.LoginForm;
import com.hotelapi.dto.LoginResponse;
import com.hotelapi.service.JwtService;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Hidden;

@Hidden
@RestController
@RequestMapping("/api/auth") 
@CrossOrigin(origins = "http://localhost:3000") // to connect please metion ur react port
public class LoginController {

    private final JwtService jwtService;

    public LoginController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

   @PostMapping("/login")
public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginForm loginForm) {
    if ("test@example.com".equalsIgnoreCase(loginForm.getEmail()) &&
        "password".equals(loginForm.getPassword())) {

        String accessToken = "token";
        String refreshToken = "token";

        return ResponseEntity.ok(new LoginResponse(
                true, "Login successful", accessToken, refreshToken
        ));
    }

    return ResponseEntity.status(401).body(new LoginResponse(
            false, "Invalid email or password", null, null
    ));
}}