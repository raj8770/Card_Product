package com.example.Demo_Product.jwt;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.JavaMailSender;

import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender gmailMailSender;

    @Autowired
    private JavaMailSender office365MailSender;

    // Method to send OTP via Gmail or Office 365
    public void sendOtpEmail(String toEmail, String otp) {
        try {
            // Create MimeMessage for Gmail or Office 365
            MimeMessage message = null;
            MimeMessageHelper helper = null;

            if (toEmail.endsWith("@gmail.com") || toEmail.endsWith("@googlemail.com")) {
                message = gmailMailSender.createMimeMessage();
                helper = new MimeMessageHelper(message, true);
                helper.setFrom("rajsumit7200@gmail.com");  // Gmail address
                helper.setTo(toEmail);
                helper.setSubject("Your OTP Code");
                helper.setText("Your OTP for login is: " + otp);
                gmailMailSender.send(message);
            } else if (toEmail.endsWith("@ahomtech.com")) {
                message = office365MailSender.createMimeMessage();
                helper = new MimeMessageHelper(message, true);
                helper.setFrom("sumit.raj@ahomtech.com");  // Office 365 email
                helper.setTo(toEmail);
                helper.setSubject("Your OTP Code");
                helper.setText("Your OTP for login is: " + otp);
                office365MailSender.send(message);
            } else {
                throw new RuntimeException("Unsupported email domain");
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Log or notify the user about the error
        }
    }


}
