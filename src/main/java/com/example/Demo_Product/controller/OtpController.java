package com.example.Demo_Product.controller;

import com.example.Demo_Product.jwt.JwtTokenProvider;
import com.example.Demo_Product.model.*;
import com.example.Demo_Product.repo.UserRepository;
import com.example.Demo_Product.service.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/auth")
public class OtpController {

    @Autowired
    OtpService otpService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserRepository userRepository;


    //Register new user

    @PostMapping("/register")
    public ResponseEntity<ResponseDTO> registerUser(@RequestBody User user) {
        try {
            ResponseDTO response = otpService.registerUser(user);

            if (!response.isSuccess()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            ResponseDTO errorResponse = new ResponseDTO();
            errorResponse.setSuccess(false);
            errorResponse.setMessage("Internal server error: " + e.getMessage());
            errorResponse.setData(null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }




    // Send otp in your register mail id
    @PostMapping("/send-otp")
    public ResponseEntity<Map<String, String>> sendOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        Map<String, String> response = otpService.sendOtp(email);
        return ResponseEntity.ok(response);
    }


    // Verify otp in your mail

    @PostMapping("/verify-otp")
    public ResponseEntity<ResponseDTO> verifyOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String otp = request.get("otp");

        if (email == null || email.isEmpty() || otp == null || otp.isEmpty()) {
            return createErrorResponse(false, "Email and OTP are required.", HttpStatus.BAD_REQUEST);
        }

        try {
            // Verify OTP
            if (otpService.verifyOtp(email, otp)) {
                User user = userRepository.findByEmail(email)
                        .orElseThrow(() -> new RuntimeException("User not found"));

                String access_token = otpService.generateJwtToken(user); // Generate JWT token
                long expiresIn = 600; // Token expiry time in seconds (10 minutes)

                // Prepare the response using DTOs
                ResponseDTO response = new ResponseDTO();
                response.setSuccess(true);
                response.setMessage("Email verified successfully");

                // Set User Data
                UserDTO userData = new UserDTO();
                userData.setId(user.getId());
                userData.setName(user.getFirstName() + " " + user.getLastName()); // Full name as 'name'
                userData.setEmail(user.getEmail());
                userData.setFirstName(user.getFirstName());
                userData.setLastName(user.getLastName());

                // Set Token Data
                TokenDTO tokenData = new TokenDTO();
                tokenData.setAccess_token(access_token);
                tokenData.setTokenType("bearer");
                tokenData.setExpiresIn(expiresIn);

                // Set DataDTO (user + token)
                DataDTO data = new DataDTO();
                data.setUser(userData);
                data.setToken(tokenData);

                response.setData(data);

                return ResponseEntity.ok(response); // Success response
            } else {
                return createErrorResponse(false, "Invalid or expired OTP.", HttpStatus.FORBIDDEN); // OTP invalid
            }
        } catch (Exception e) {
            return createErrorResponse(false, "Something went wrong.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private ResponseEntity<ResponseDTO> createErrorResponse(boolean success, String message, HttpStatus status) {
        ResponseDTO response = new ResponseDTO();
        response.setSuccess(success);
        response.setMessage(message);
        response.setData(null);
        return ResponseEntity.status(status).body(response);
    }
}
