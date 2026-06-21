package com.tradeguard.service;

import com.tradeguard.entity.User;
import com.tradeguard.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class UserService {
	  
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public UserService(UserRepository userRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public boolean registerUser(String fullName, String email, String password, String plan) {
        if (userRepository.existsByEmail(email)) {
            return false;
        }

        User user = new User();
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(plan != null && plan.equals("PRO") ? "PRO" : "FREE");
        user.setEnabled(false);
        user.setPaymentDone(false);  // ⭐ NEW
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        
        if (plan != null && plan.equals("PRO")) {
            user.setVirtualBalance(10000.0);
        } else {
            user.setVirtualBalance(1000.0);
        }

        String code = generateVerificationCode();
        user.setVerificationCode(code);
        user.setVerificationCodeExpiry(LocalDateTime.now().plusMinutes(15));

        userRepository.save(user);
        emailService.sendVerificationEmail(email, code);
        return true;
    }

    public boolean verifyEmail(String email, String code) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) return false;

        User user = userOpt.get();
        if (user.isEnabled()) return false;

        if (user.getVerificationCode().equals(code.trim()) && 
            user.getVerificationCodeExpiry().isAfter(LocalDateTime.now())) {
            user.setEnabled(true);
            user.setVerificationCode(null);
            user.setVerificationCodeExpiry(null);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User updateProfile(User user) {
        user.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    // ⭐⭐⭐ UPGRADE TO PRO - YE METHOD ADD KARO ⭐⭐⭐
//    public User upgradeToPro(String email) {
//        User user = userRepository.findByEmail(email)
//            .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
//        
//        user.upgradeToPro();  // User class ka method call kar raha hai
//        user.setUpdatedAt(LocalDateTime.now());
//        
//        System.out.println("✅ User upgraded to PRO: " + email);
//        System.out.println("💰 New Balance: $" + user.getVirtualBalance());
//        System.out.println("💳 Payment Done: " + user.isPaymentDone());
//        
//        return userRepository.save(user);
//    }
 // ⭐ UPGRADE TO PRO
    public User upgradeToPro(String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        
        user.setRole("PRO");
        user.setVirtualBalance(10000.0);
        user.setPaymentDone(true);
        user.setUpdatedAt(LocalDateTime.now());
        
        System.out.println("✅ User upgraded to PRO: " + email);
        System.out.println("💰 New Balance: $" + user.getVirtualBalance());
        
        return userRepository.save(user);
    }
    
    

    private String generateVerificationCode() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(999999));
    }
}