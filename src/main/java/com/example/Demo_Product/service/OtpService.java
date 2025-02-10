package com.example.Demo_Product.service;

import com.example.Demo_Product.jwt.EmailService;
import com.example.Demo_Product.jwt.JwtTokenProvider;
import com.example.Demo_Product.model.ResponseDTO;
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
    private JavaMailSender javaMailSender;

    @Autowired
    private EmailService emailService;


    // Register new User

    public ResponseDTO registerUser(User user) {
        ResponseDTO response = new ResponseDTO();

        try {
            // Check if the email already exists
            if (userRepository.findByEmail(user.getEmail()).isPresent()) {
                response.setSuccess(false);
                response.setMessage("Email is already registered.");
                response.setData(null);
                return response;
            }

            // Set full name by combining first and last names
            user.setName(user.getFirstName() + " " + user.getLastName());
            userRepository.save(user);

            response.setSuccess(true);
            response.setMessage("User registered successfully.");
            response.setData(null);
            return response;

        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("An error occurred while registering the user: " + e.getMessage());
            response.setData(null);
            return response;
        }
    }



    // Method to send OTP

    public Map<String, String> sendOtp(String email) {
        String otp = String.valueOf(new Random().nextInt(9000) + 1000); // 4-digit OTP

        // Check if user exists or create a new one
        User user = userRepository.findByEmail(email).orElse(new User());
        user.setEmail(email);
        user.setOtp(otp);
        user.setOtpExpiry(LocalDateTime.now().plusMinutes(5));
        user.setVerified(false);

        userRepository.save(user);  // Save or update user in DB
        emailService.sendOtpEmail(email, otp);  // Send OTP via email

        Map<String, String> response = new HashMap<>();
        response.put("message", "The OTP has been sent successfully. Only valid for 5 minutes.");
        return response;
    }

    // Method to generate JWT token
    public String generateJwtToken(User user) {
        return jwtTokenProvider.generateToken(user.getEmail());  // JWT Token generate karein
    }



    // Verify OTP
    public boolean verifyOtp(String email, String otp) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Check if OTP matches and if it is expired
        if (user.getOtp() == null || !user.getOtp().trim().equals(otp.trim())) {
            return false; // Invalid OTP
        }

        if (user.getOtpExpiry().isBefore(LocalDateTime.now())) {
            return false; // OTP expired
        }

        // OTP is valid, remove OTP and expiry from DB
        user.setVerified(true);
        user.setOtp(null);
        user.setOtpExpiry(null);

        userRepository.save(user); // Save user with OTP removed
        return true;
    }
}
