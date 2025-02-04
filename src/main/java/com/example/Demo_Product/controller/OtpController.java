package com.example.Demo_Product.controller;

import com.example.Demo_Product.jwt.JwtTokenProvider;
import com.example.Demo_Product.model.User;
import com.example.Demo_Product.repo.UserRepository;
import com.example.Demo_Product.service.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class OtpController {

    @Autowired
    private OtpService otpService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    UserRepository userRepository;

    @PostMapping("/send-otp")
    public ResponseEntity<Map<String, String>> sendOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        Map<String, String> response = otpService.sendOtp(email);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<Map<String, String>> verifyOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");  // Email request se lo
        String otp = request.get("otp");

        if (email == null || email.isEmpty() || otp == null || otp.isEmpty()) {
            return createErrorResponse("Email and OTP are required.", HttpStatus.BAD_REQUEST);
        }

        try {
            if (otpService.verifyOtp(email, otp)) {
                User user = userRepository.findByEmail(email)
                        .orElseThrow(() -> new RuntimeException("User not found"));

                String token = otpService.generateJwtToken(user);  // JWT Token generate karein

                Map<String, String> response = new HashMap<>();
                response.put("message", "OTP verified successfully.");
                response.put("token", token);  // JWT Token response me bhej rahe hain

                return ResponseEntity.ok(response);
            } else {
                return createErrorResponse("Invalid or expired OTP.", HttpStatus.FORBIDDEN);
            }
        } catch (Exception e) {
            return createErrorResponse("Something went wrong.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private ResponseEntity<Map<String, String>> createErrorResponse(String message, HttpStatus status) {
        Map<String, String> response = Map.of("error", message);
        return ResponseEntity.status(status).body(response);
    }
}
