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

    public ResponseDTO registerUser(User user) {
        // Full name is set by combining first and last names
        user.setName(user.getFirstName() + " " + user.getLastName());
        userRepository.save(user);

        // Create the ResponseDTO
        ResponseDTO response = new ResponseDTO();
        response.setSuccess(true);
        response.setMessage("User registered successfully");

        // You can leave the data part null or create an empty DataDTO if needed
        response.setData(null); // Or set an empty DataDTO if you prefer

        return response;
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
