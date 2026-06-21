package com.tradeguard.controller;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tradeguard.entity.User;
import com.tradeguard.service.UserService;

@Controller
public class ProfileController {
	   private final UserService userService;
	    
	    public ProfileController(UserService userService) {
	        this.userService = userService;
	    }
	    
	    @GetMapping("/profile")
	    public String showProfile(Authentication authentication, Model model) {
	        String email = authentication.getName();
	        User user = userService.getUserByEmail(email).orElse(null);
	        model.addAttribute("user", user);
	        return "profile";
	    }
	    
	    @PostMapping("/profile/update")
	    public String updateProfile(@RequestParam String fullName,
	                               @RequestParam String phoneNumber,
	                               @RequestParam String country,
	                               Authentication authentication) {
	        String email = authentication.getName();
	        User user = userService.getUserByEmail(email).orElse(null);
	        
	        if (user != null) {
	            user.setFullName(fullName);
	            user.setPhoneNumber(phoneNumber);
	            user.setCountry(country);
	            userService.updateProfile(user);
	        }
	        
	        return "redirect:/profile?success=true";
	    }
	}
