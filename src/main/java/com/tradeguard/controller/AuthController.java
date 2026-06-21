package com.tradeguard.controller;
import com.tradeguard.dto.LoginRequest;
import com.tradeguard.dto.RegisterRequest;
import com.tradeguard.dto.VerifyRequest;
import com.tradeguard.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {
	  private final UserService userService;
	    
	    public AuthController(UserService userService) {
	        this.userService = userService;
	    }
	    
	    @GetMapping("/register")
	    public String showRegisterForm(Model model) {
	        model.addAttribute("registerRequest", new RegisterRequest());
	        return "register";
	    }
	    
	    @PostMapping("/register")
	    public String register(@Valid @ModelAttribute RegisterRequest request, 
	                          BindingResult result, Model model) {
	        if (result.hasErrors()) {
	            return "register";
	        }
	        
	        boolean success = userService.registerUser(
	            request.getFullName(),
	            request.getEmail(),
	            request.getPassword(),
	            request.getPlan()
	        );
	        
	        if (!success) {
	            model.addAttribute("error", "Email already registered!");
	            return "register";
	        }
	        
	        String email = request.getEmail();
	        System.out.println("📧 Registered user: " + email);
	        
	        // ⭐ ALWAYS GO TO VERIFICATION FIRST
	        return "redirect:/verify?email=" + email;
	    }
	    
	    @GetMapping("/login")
	    public String showLoginForm() {
	        return "login";
	    }
	    
	    @GetMapping("/verify")
	    public String showVerifyForm(@RequestParam(required = false) String email, Model model) {
	        model.addAttribute("email", email);
	        return "verify";
	    }
	    
	    @PostMapping("/verify")
	    public String verifyEmail(
	            @RequestParam(required = false) String email,
	            @RequestParam(required = false) String code,
	            Model model) {
	        
	        if (email == null || email.isEmpty()) {
	            model.addAttribute("error", "Email is required!");
	            return "verify";
	        }
	        
	        if (code == null || code.isEmpty()) {
	            model.addAttribute("error", "Verification code is required!");
	            model.addAttribute("email", email);
	            return "verify";
	        }
	        
	        boolean verified = userService.verifyEmail(email, code);
	        
	        if (!verified) {
	            model.addAttribute("error", "Invalid or expired verification code!");
	            model.addAttribute("email", email);
	            return "verify";
	        }
	        
	        model.addAttribute("message", "Email verified successfully! Please login.");
	        return "login";
	    }
	}
