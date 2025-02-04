//package com.example.Demo_Product.jwt;
//
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import java.util.Collection;
//
//public class CustomUserDetails implements UserDetails {
//
//    private String email;
//    private boolean verified;
//
//    public CustomUserDetails(String email, boolean verified) {
//        this.email = email;
//        this.verified = verified;
//    }
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return null; // You can return roles or authorities if you need them
//    }
//
//    @Override
//    public String getPassword() {
//        return null; // No password, as we're using OTP for login
//    }
//
//    @Override
//    public String getUsername() {
//        return email;  // Use email as the username
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;  // You can add logic for account expiration if needed
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;  // You can add logic for account locking if needed
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;  // No password, so no expiration here
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return verified;  // The user is enabled only if they are verified (OTP verified)
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public boolean isVerified() {
//        return verified;
//    }
//}
//
