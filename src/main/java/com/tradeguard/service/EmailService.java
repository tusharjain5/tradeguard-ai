package com.tradeguard.service;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
public class EmailService {

private final JavaMailSender mailSender;
    
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }
    
    public void sendVerificationEmail(String to, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("TradeGuard AI - Email Verification");
        message.setText("Hello,\n\n" +
                       "Thank you for registering with TradeGuard AI!\n\n" +
                       "Your verification code is: " + code + "\n\n" +
                       "Please enter this code to verify your email address.\n\n" +
                       "This code will expire in 15 minutes.\n\n" +
                       "Best regards,\n" +
                       "TradeGuard AI Team");
        mailSender.send(message);
    }
}