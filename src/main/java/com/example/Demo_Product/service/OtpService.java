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
        String otp = String.valueOf(new Random().nextInt(900000) + 100000);

        User user = userRepository.findByEmail(email).orElse(new User());
        user.setEmail(email);
        user.setOtp(otp);
        user.setOtpExpiry(LocalDateTime.now().plusMinutes(5));
        user.setVerified(false);

        userRepository.save(user);
        emailService.sendOtpEmail(email, otp);  // Send OTP via email

        Map<String, String> response = new HashMap<>();
        response.put("email", email);
        response.put("message", "OTP sent successfully.");
        return response;
    }

    public boolean verifyOtp(String email, String otp) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Log OTP and stored OTP for debugging
        System.out.println("Verifying OTP for email: " + email);
        System.out.println("Entered OTP: " + otp);
        System.out.println("Stored OTP: " + user.getOtp());

        if (user.getOtp().equals(otp) && user.getOtpExpiry().isAfter(LocalDateTime.now())) {
            user.setVerified(true);
            userRepository.save(user);
            return true;
        } else {
            return false;
        }
    }
}
