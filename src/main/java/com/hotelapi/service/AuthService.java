package com.hotelapi.service;

import com.hotelapi.dto.LoginRequest;
import com.hotelapi.dto.LoginResponse;
import com.hotelapi.dto.RegisterRequest;
import com.hotelapi.dto.ForgotPasswordRequest;
import com.hotelapi.dto.VerifyOtpRequest;
import com.hotelapi.dto.ResetPasswordRequest;
import com.hotelapi.entity.User;
import com.hotelapi.entity.OtpVerification;
import com.hotelapi.repository.UserRepository;

import com.hotelapi.repository.OtpVerificationRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final OtpVerificationRepository otpVerificationRepository;
    private final EmailService emailService;

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

    public String sendOtp(ForgotPasswordRequest request) {
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found with email: " + request.getEmail());
        }

        String otp = generateOtp();
        LocalDateTime expiry = LocalDateTime.now().plusMinutes(5);

        OtpVerification otpEntity = OtpVerification.builder()
                .email(request.getEmail())
                .otp(otp)
                .expiryTime(expiry)
                .verified(false)
                .build();

        otpVerificationRepository.save(otpEntity);
        emailService.sendOtpEmail(request.getEmail(), otp, 5);

        return "OTP sent to email.";
    }

    public String verifyOtp(VerifyOtpRequest request) {
        OtpVerification otpRecord = otpVerificationRepository
                .findTopByEmailOrderByCreatedAtDesc(request.getEmail())
                .orElseThrow(() -> new RuntimeException("OTP not found for email."));

        if (otpRecord.isVerified()) {
            return "OTP already verified.";
        }

        if (!otpRecord.getOtp().equals(request.getOtp())) {
            throw new RuntimeException("Invalid OTP.");
        }

        if (otpRecord.getExpiryTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("OTP has expired.");
        }

        otpRecord.setVerified(true);
        otpVerificationRepository.save(otpRecord);

        return "OTP verified successfully.";
    }

    
    
//    public String resetPassword(ResetPasswordRequest request) {
//        OtpVerification otpRecord = otpVerificationRepository
//                .findTopByEmailOrderByCreatedAtDesc(request.getEmail())
//                .orElseThrow(() -> new RuntimeException("OTP record not found."));
//
//        System.out.println(" Received email: " + request.getEmail());
//        System.out.println(" Received OTP: " + request.getOtp());
//        System.out.println(" Stored OTP: " + otpRecord.getOtp());
//        System.out.println(" Is Verified: " + otpRecord.isVerified());
//
//        if (!otpRecord.isVerified()) {
//            throw new RuntimeException("OTP has not been verified.");
//        }
//
//        //  Do NOT compare OTP again here — it's already verified
//
//        User user = userRepository.findByEmail(request.getEmail())
//                .orElseThrow(() -> new RuntimeException("User not found."));
//
//        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
//        userRepository.save(user);
//
//        return "Password reset successfully.";
//    }
 
    
    public String resetPassword(ResetPasswordRequest request) {
        OtpVerification otpRecord = otpVerificationRepository
                .findTopByEmailOrderByCreatedAtDesc(request.getEmail())
                .orElseThrow(() -> new RuntimeException("OTP record not found."));

        System.out.println(" Received email: " + request.getEmail());
        System.out.println(" Received OTP: " + request.getOtp());
        System.out.println(" Stored OTP: " + otpRecord.getOtp());
        System.out.println(" Is Verified: " + otpRecord.isVerified());

        // ✅ First: OTP must be marked as verified
        if (!otpRecord.isVerified()) {
            throw new RuntimeException("OTP has not been verified.");
        }

        // ✅ Then: Make sure the OTP in request matches the verified one
        if (!otpRecord.getOtp().equals(request.getOtp())) {
            throw new RuntimeException("OTP does not match the verified one.");
        }

        // ✅ If everything is valid, update password
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found."));

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        return "Password reset successfully.";
    }



    private String generateOtp() {
        return String.valueOf(100000 + new Random().nextInt(900000));
    }

	
}














//package com.hotelapi.service;
//
//import com.hotelapi.dto.LoginRequest;
//import com.hotelapi.dto.LoginResponse;
//import com.hotelapi.dto.RegisterRequest;
//import com.hotelapi.entity.User;
//import com.hotelapi.repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class AuthService {
//
//  private final UserRepository userRepository;
//  private final PasswordEncoder passwordEncoder;
//  private final JwtService jwtService;
//
//  public String register(RegisterRequest request) {
//      if (userRepository.existsByEmail(request.getEmail())) {
//          throw new RuntimeException("Email already in use");
//      }
//
//      var user = new User();
//      user.setEmail(request.getEmail());
//      user.setPassword(passwordEncoder.encode(request.getPassword()));
//      userRepository.save(user);
//
//      return "User registered successfully";
//  }
//
//  public LoginResponse login(LoginRequest loginRequest) {
//      var user = userRepository.findByEmail(loginRequest.getEmail())
//              .orElseThrow(() -> new RuntimeException("Invalid email or password"));
//
//      if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
//          throw new RuntimeException("Invalid email or password");
//      }
//
//       String accessToken = jwtService.generateAccessToken(user.getEmail());
//       String refreshToken = jwtService.generateRefreshToken(user.getEmail());
//
//      return new LoginResponse(true, "Login successful", accessToken, refreshToken);
//  }
//}