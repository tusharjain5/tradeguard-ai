package com.tradeguard.service;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.tradeguard.entity.User;
import com.tradeguard.repository.UserRepository;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    
    private final UserRepository userRepository;
    
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("=========================================");
        System.out.println("🔍 loadUserByUsername CALLED");
        System.out.println("📧 Email: " + email);
        System.out.println("=========================================");
        
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> {
                System.out.println("❌ User NOT found with email: " + email);
                return new UsernameNotFoundException("User not found with email: " + email);
            });
        
        System.out.println("✅ User found: " + user.getEmail());
        System.out.println("🔓 Enabled: " + user.isEnabled());
        System.out.println("🔐 Stored Password (encoded): " + user.getPassword());
        System.out.println("👤 Role: " + user.getRole());
        
        if (!user.isEnabled()) {
            System.out.println("❌ User is NOT enabled!");
            throw new UsernameNotFoundException("User not verified");
        }
        
        System.out.println("✅ User is enabled! Creating UserDetails...");
        
        return new org.springframework.security.core.userdetails.User(
            user.getEmail(),
            user.getPassword(),
            Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole()))
        );
    }
}