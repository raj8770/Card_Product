package com.example.Demo_Product.service;

import com.example.Demo_Product.jwt.EmailService;
import com.example.Demo_Product.jwt.JwtTokenProvider;
import com.example.Demo_Product.model.User;
import com.example.Demo_Product.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class OtpService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    JavaMailSender javaMailSender;

    @Autowired
    EmailService emailService;

    public Map<String, String> sendOtp(String email) {
        String otp = String.valueOf(new Random().nextInt(9000) + 1000); // 4-digit OTP

        User user = userRepository.findByEmail(email).orElse(new User());
        user.setEmail(email);
        user.setOtp(otp);
        user.setOtpExpiry(LocalDateTime.now().plusMinutes(5));
        user.setVerified(false);

        userRepository.save(user);
        emailService.sendOtpEmail(email, otp);  // Email OTP send

        Map<String, String> response = new HashMap<>();
        response.put("message", "The OTP has been sent successfully. Only valid for 5 minutes.");
        return response;
    }


    public String generateJwtToken(User user) {
        return jwtTokenProvider.generateToken(user.getEmail());  // JWT Token generate karein
    }

    public boolean verifyOtp(String email, String otp) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        System.out.println("Entered OTP: " + otp);
        System.out.println("Stored OTP: " + user.getOtp());

        // Check if OTP is correct
        if (user.getOtp() == null || !user.getOtp().trim().equals(otp.trim())) {
            return false;
        }

        // Check if OTP is expired
        if (user.getOtpExpiry().isBefore(LocalDateTime.now())) {
            return false;
        }

        // ✅ OTP is verified, now remove OTP and expiry from database
        user.setVerified(true);
        user.setOtp(null);  // Remove OTP from DB
        user.setOtpExpiry(null);  // Remove OTP expiry from DB

        userRepository.save(user);  // Save user without OTP

        return true;
    }

}
