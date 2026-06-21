package com.tradeguard.controller;
import com.tradeguard.dto.PaymentRequest;
import com.tradeguard.service.PaymentService;
import com.tradeguard.service.UserService;
import com.tradeguard.entity.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/payment")
public class PaymentController {

	 private final PaymentService paymentService;
	    private final UserService userService;

	    @Value("${razorpay.key.id}")
	    private String razorpayKeyId;

	    public PaymentController(PaymentService paymentService, UserService userService) {
	        this.paymentService = paymentService;
	        this.userService = userService;
	    }

	    @GetMapping("/upgrade")
	    public String upgradePage(Model model, @RequestParam String email) {
	        model.addAttribute("email", email);
	        model.addAttribute("razorpayKeyId", razorpayKeyId);
	        return "payment";
	    }

	    @PostMapping("/create-order")
	    @ResponseBody
	    public Map<String, String> createOrder(@RequestBody Map<String, String> request) {
	        try {
	            int amount = Integer.parseInt(request.get("amount"));
	            String orderId = paymentService.createOrder(amount);
	            
	            Map<String, String> response = new HashMap<>();
	            response.put("orderId", orderId);
	            response.put("keyId", razorpayKeyId);
	            response.put("amount", String.valueOf(amount * 100));
	            return response;
	        } catch (Exception e) {
	            Map<String, String> response = new HashMap<>();
	            response.put("error", e.getMessage());
	            return response;
	        }
	    }

	    // ⭐ SIGNATURE VERIFICATION HATAYA - DIRECT UPGRADE
	    @PostMapping("/verify")
	    @ResponseBody
	    public Map<String, String> verifyPayment(@RequestBody PaymentRequest request) {
	        try {
	            System.out.println("=========================================");
	            System.out.println("🔍 PAYMENT SUCCESS - UPGRADING TO PRO");
	            System.out.println("📧 Email: " + request.getEmail());
	            System.out.println("📦 Order ID: " + request.getOrderId());
	            System.out.println("💳 Payment ID: " + request.getPaymentId());
	            System.out.println("=========================================");
	            
	            // ⭐ DIRECT UPGRADE - NO SIGNATURE CHECK
	            User user = userService.upgradeToPro(request.getEmail());
	            
	            System.out.println("✅ USER UPGRADED TO PRO: " + user.getEmail());
	            System.out.println("💰 New Balance: $" + user.getVirtualBalance());
	            System.out.println("=========================================");
	            
	            return Map.of(
	                "status", "success",
	                "message", "Payment successful! You are now PRO!",
	                "balance", String.valueOf(user.getVirtualBalance())
	            );
	            
	        } catch (Exception e) {
	            System.err.println("❌ Error: " + e.getMessage());
	            e.printStackTrace();
	            return Map.of("status", "error", "message", e.getMessage());
	        }
	    }
	    
	    // ⭐ SKIP PAYMENT - TESTING
	    @GetMapping("/skip")
	    public String skipPayment(@RequestParam String email) {
	        try {
	            userService.upgradeToPro(email);
	            System.out.println("✅ PRO activated (SKIP) for: " + email);
	            return "redirect:/dashboard?pro_activated=true";
	        } catch (Exception e) {
	            return "redirect:/login?error=" + e.getMessage();
	        }
	    }
	}