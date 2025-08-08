package com.hotelapi.service;

import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendOtpEmail(String to, String otp, int expiryMinutes) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            // ✅ Set recipient
            helper.setTo(to);

            // ✅ Set subject
            helper.setSubject("Password Reset OTP");

            // ✅ Set plain text body
            String body = "Your OTP is: " + otp + "\n\n"
                    + "It will expire in " + expiryMinutes + " minutes.\n"
                    + "Do not share this code with anyone.";
            helper.setText(body, false); // false = plain text

            // ✅ Set from address with display name
            helper.setFrom(new InternetAddress("testing@alphaseam.com", "AlphaSeam Support"));

            mailSender.send(message);
            System.out.println("Email sent to: " + to);
        } catch (Exception e) {
            System.err.println("Failed to send email to: " + to);
            e.printStackTrace();
        }
    }
}

