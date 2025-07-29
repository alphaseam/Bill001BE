package com.hotelapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendOtpEmail(String to, String otp, int expiryMinutes) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Password Reset OTP");
        message.setText("Your OTP is: " + otp + "\n\n"
            + "It will expire in " + expiryMinutes + " minutes.\n"
            + "Do not share this code with anyone.");

        mailSender.send(message);
    }

	
}
