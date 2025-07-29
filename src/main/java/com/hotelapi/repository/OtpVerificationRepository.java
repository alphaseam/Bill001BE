package com.hotelapi.repository;

import com.hotelapi.entity.OtpVerification;
import com.hotelapi.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface OtpVerificationRepository extends JpaRepository<OtpVerification, UUID> {

    Optional<OtpVerification> findTopByEmailOrderByCreatedAtDesc(String email);

    Optional<OtpVerification> findTopByEmailAndOtpAndVerifiedFalseOrderByCreatedAtDesc(String email, String otp);

    long countByEmailAndCreatedAtAfter(String email, LocalDateTime since);

	
}

