package com.example.Demo_Product.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", nullable = false, unique = true)  // Ensure email is unique and not null
    private String email;

    @Column(name = "otp")
    private String otp;

    @Column(name = "otp_expiry")
    private LocalDateTime otpExpiry;

    @Column(name = "verified")
    private boolean verified;

    @Column(name = "first_name")  // Added first name
    private String firstName;

    @Column(name = "last_name")  // Added last name
    private String lastName;

    @Transient  // Added a transient field for full name (not persisted in DB)
    private String name;

    // Default Constructor
    public User() {
    }

    // Constructor with fields
    public User(Long id, String email, String otp, LocalDateTime otpExpiry, boolean verified, String firstName, String lastName) {
        this.id = id;
        this.email = email;
        this.otp = otp;
        this.otpExpiry = otpExpiry;
        this.verified = verified;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    // Getter and Setter methods
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public LocalDateTime getOtpExpiry() {
        return otpExpiry;
    }

    public void setOtpExpiry(LocalDateTime otpExpiry) {
        this.otpExpiry = otpExpiry;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getName() {
        // Combining first and last name into full name
        return this.firstName + " " + this.lastName;
    }

    public void setName(String name) {
        this.name = name;
    }
}
