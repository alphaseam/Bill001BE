package com.hotelapi.service;

import com.hotelapi.dto.LoginRequest;
import com.hotelapi.dto.LoginResponse;
import com.hotelapi.dto.RegisterRequest;
import com.hotelapi.entity.User;
import com.hotelapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public String register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already in use");
        }

        var user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);

        return "User registered successfully";
    }

    public LoginResponse login(LoginRequest loginRequest) {
        var user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

         String accessToken = jwtService.generateAccessToken(user.getEmail());
         String refreshToken = jwtService.generateRefreshToken(user.getEmail());

        return new LoginResponse(true, "Login successful", accessToken, refreshToken);
    }
}
