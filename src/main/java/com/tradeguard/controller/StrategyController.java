package com.tradeguard.controller;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.tradeguard.dto.TradingDecision;
import com.tradeguard.entity.User;
import com.tradeguard.service.MarketDataService;
import com.tradeguard.service.StrategyService;
import com.tradeguard.service.UserService;



@Controller
public class StrategyController {

	   private final MarketDataService marketDataService;
	    private final StrategyService strategyService;
	    private final UserService userService;

	    public StrategyController(MarketDataService marketDataService,
	                              StrategyService strategyService,
	                              UserService userService) {
	        this.marketDataService = marketDataService;
	        this.strategyService = strategyService;
	        this.userService = userService;
	    }

	    @GetMapping("/strategy")
	    public String strategy(Model model) {
	        try {
	            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	            String email = authentication.getName();
	            User user = userService.getUserByEmail(email).orElse(null);

	            // ⭐ FREE USER CHECK - Redirect to dashboard
	            if (user == null || user.getRole().equals("FREE")) {
	                System.out.println("❌ FREE user attempted to access Strategy page!");
	                return "redirect:/dashboard";
	            }

	            String btcPrice = marketDataService.getBTCPrice();
	            double price = Double.parseDouble(btcPrice);
	            TradingDecision decision = strategyService.analyzeMarket(price);

	            model.addAttribute("btcPrice", String.format("$%.2f", price));
	            model.addAttribute("signal", decision.getSignal());
	            model.addAttribute("confidence", decision.getConfidence());
	            model.addAttribute("risk", decision.getRisk());
	            model.addAttribute("userRole", user != null ? user.getRole() : "FREE");

	            return "strategy";
	        } catch (Exception e) {
	            System.out.println("❌ Strategy Error: " + e.getMessage());
	            return "redirect:/dashboard";
	        }
	    }
	}
