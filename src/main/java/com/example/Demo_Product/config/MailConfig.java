package com.example.Demo_Product.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {

    // Load values from application.properties
    @Value("${spring.mail.gmail.username}")
    private String gmailUsername;

    @Value("${spring.mail.gmail.password}")
    private String gmailPassword;

    @Value("${spring.mail.office365.username}")
    private String office365Username;

    @Value("${spring.mail.office365.password}")
    private String office365Password;

    @Bean
    @Primary // Make Gmail the primary mail sender
    public JavaMailSender gmailMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        mailSender.setUsername(gmailUsername);
        mailSender.setPassword(gmailPassword);
        mailSender.setJavaMailProperties(getMailProperties());
        return mailSender;
    }

    @Bean
    public JavaMailSender office365MailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.office365.com");
        mailSender.setPort(587);
        mailSender.setUsername(office365Username);
        mailSender.setPassword(office365Password);
        mailSender.setJavaMailProperties(getMailProperties());
        return mailSender;
    }

    // Common mail properties for both Gmail and Office365
    private Properties getMailProperties() {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        return properties;
    }
}
