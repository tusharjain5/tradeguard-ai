package com.tradeguard.service;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Value;



@Service
public class EmailService {

//private final JavaMailSender mailSender;
//    
//    public EmailService(JavaMailSender mailSender) {
//        this.mailSender = mailSender;
//    }
//    
//    public void sendVerificationEmail(String to, String code) {
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo(to);
//        message.setSubject("TradeGuard AI - Email Verification");
//        message.setText("Hello,\n\n" +
//                       "Thank you for registering with TradeGuard AI!\n\n" +
//                       "Your verification code is: " + code + "\n\n" +
//                       "Please enter this code to verify your email address.\n\n" +
//                       "This code will expire in 15 minutes.\n\n" +
//                       "Best regards,\n" +
//                       "TradeGuard AI Team");
//        mailSender.send(message);
//    }
//    
//    
//    
	
	   @Value("${sendgrid.api.key}")
	    private String sendGridApiKey;

	    @Value("${sendgrid.from.email}")
	    private String fromEmail;

	    public void sendVerificationEmail(String to, String code) {
	        try {
	            // SendGrid classes with full package
	            com.sendgrid.helpers.mail.objects.Email from = new com.sendgrid.helpers.mail.objects.Email(fromEmail);
	            com.sendgrid.helpers.mail.objects.Email toEmail = new com.sendgrid.helpers.mail.objects.Email(to);
	            
	            String subject = "TradeGuard AI - Email Verification";
	            String content = "Hello,\n\n" +
	                           "Thank you for registering with TradeGuard AI!\n\n" +
	                           "Your verification code is: " + code + "\n\n" +
	                           "This code will expire in 15 minutes.\n\n" +
	                           "Best regards,\n" +
	                           "TradeGuard AI Team";

	            com.sendgrid.helpers.mail.objects.Content emailContent = 
	                new com.sendgrid.helpers.mail.objects.Content("text/plain", content);
	            
	            com.sendgrid.helpers.mail.Mail mail = 
	                new com.sendgrid.helpers.mail.Mail(from, subject, toEmail, emailContent);

	            com.sendgrid.SendGrid sg = new com.sendgrid.SendGrid(sendGridApiKey);
	            
	            com.sendgrid.Request request = new com.sendgrid.Request();
	            request.setMethod(com.sendgrid.Method.POST);
	            request.setEndpoint("mail/send");
	            request.setBody(mail.build());

	            com.sendgrid.Response response = sg.api(request);

	            if (response.getStatusCode() == 202) {
	                System.out.println("✅ Email sent to: " + to);
	            } else {
	                System.out.println("❌ Email failed. Status: " + response.getStatusCode());
	                System.out.println("Response: " + response.getBody());
	            }
	        } catch (Exception e) {
	            System.err.println("❌ Email error: " + e.getMessage());
	            e.printStackTrace();
	        }
	    }
	}
    
    
