package com.example.Demo_Product.controller;

import com.example.Demo_Product.jwt.JwtTokenProvider;
import com.example.Demo_Product.service.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class OtpController {

    @Autowired
    private OtpService otpService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @PostMapping("/send-otp")
    public ResponseEntity<Map<String, String>> sendOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        Map<String, String> response = otpService.sendOtp(email);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<Map<String, String>> verifyOtp(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody Map<String, String> request) {

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return createErrorResponse("JWT token is missing or invalid", HttpStatus.FORBIDDEN);
        }

        String token = authorizationHeader.substring(7); // Remove "Bearer "

        try {
            String email = jwtTokenProvider.getEmailFromJwt(token); // Get email from JWT

            if (email == null) {
                return createErrorResponse("Invalid JWT token", HttpStatus.FORBIDDEN);
            }

            String otp = request.get("otp");

            if (otpService.verifyOtp(email, otp)) {
                return createSuccessResponse("OTP verified successfully.");
            } else {
                return createErrorResponse("Invalid or expired OTP.", HttpStatus.FORBIDDEN);
            }
        } catch (Exception e) {
            return createErrorResponse("Invalid or expired JWT token.", HttpStatus.FORBIDDEN);
        }
    }

    private ResponseEntity<Map<String, String>> createSuccessResponse(String message) {
        Map<String, String> response = Map.of("message", message);
        return ResponseEntity.ok(response);
    }

    private ResponseEntity<Map<String, String>> createErrorResponse(String message, HttpStatus status) {
        Map<String, String> response = Map.of("error", message);
        return ResponseEntity.status(status).body(response);
    }
}
