package com.tradeguard.controller;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.tradeguard.dto.PaperTrade;
import com.tradeguard.dto.TradeHistory;
import com.tradeguard.dto.TradingDecision;
import com.tradeguard.entity.Trade;
import com.tradeguard.entity.User;
import com.tradeguard.service.MarketDataService;
import com.tradeguard.service.PaperTradingService;
import com.tradeguard.service.StrategyService;
import com.tradeguard.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

@Controller
public class DashboardController {


	private final MarketDataService marketDataService;
    private final StrategyService strategyService;
    private final PaperTradingService paperTradingService;
    private final UserService userService;

    public DashboardController(MarketDataService marketDataService, 
                               StrategyService strategyService,
                               PaperTradingService paperTradingService, 
                               UserService userService) {
        this.marketDataService = marketDataService;
        this.strategyService = strategyService;
        this.paperTradingService = paperTradingService;
        this.userService = userService;
    }
    
    @GetMapping("/")
    public String home() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            
            if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
                String email = auth.getName();
                User user = userService.getUserByEmail(email).orElse(null);
                if (user != null && user.isEnabled()) {
                    return "redirect:/dashboard";
                }
            }
            return "home";
        } catch (Exception e) {
            System.out.println("❌ Home page error: " + e.getMessage());
            return "home";
        }
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        try {
            System.out.println("=========================================");
            System.out.println("🔍 DASHBOARD PAGE ACCESSED");
            
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            
            if (authentication == null || !authentication.isAuthenticated()) {
                System.out.println("❌ User NOT authenticated! Redirecting to login...");
                return "redirect:/login";
            }
            
            String email = authentication.getName();
            System.out.println("✅ Authenticated user: " + email);
            
            User user = userService.getUserByEmail(email).orElse(null);
            if (user == null) {
                System.out.println("❌ User NOT found in database!");
                SecurityContextHolder.clearContext();
                return "redirect:/login?error=user_not_found";
            }
            
            if (!user.isEnabled()) {
                System.out.println("❌ User NOT verified!");
                return "redirect:/verify?email=" + email;
            }
            
            // ⭐ CHECK: PRO user with no payment
            if (user.getRole().equals("PRO") && !user.isPaymentDone()) {
                System.out.println("💎 PRO user - Payment pending! Redirecting to payment...");
                return "redirect:/payment/upgrade?email=" + email;
            }
            
            System.out.println("✅ User found: " + user.getEmail());
            System.out.println("💰 Balance: " + user.getVirtualBalance());
            System.out.println("👤 Role: " + user.getRole());
            
            Double balance = user.getVirtualBalance();
            String btcPrice = marketDataService.getBTCPrice();
            double price = Double.parseDouble(btcPrice);
            TradingDecision decision = strategyService.analyzeMarket(price);
            
            PaperTrade paperTrade = new PaperTrade(
                decision.getSignal(),
                String.format("$%.2f", price),
                "$0.00",
                "Click BUY to start trading"
            );
            
            List<Trade> openTrades = paperTradingService.getOpenTrades();
            List<Trade> tradeHistory = paperTradingService.getTradeHistory();
            Double totalPnl = paperTradingService.getTotalPnL();

            model.addAttribute("tradeHistory", tradeHistory);
            model.addAttribute("paperTrade", paperTrade);
            model.addAttribute("btcPrice", String.format("$%.2f", price));
            model.addAttribute("signal", decision.getSignal());
            model.addAttribute("confidence", decision.getConfidence());
            model.addAttribute("risk", decision.getRisk());
            model.addAttribute("lastUpdated", LocalDateTime.now());
            model.addAttribute("topGainer", "BTC +2.4%");
            model.addAttribute("topLoser", "DOGE -4.1%");
            model.addAttribute("marketMood", "Bullish");
            model.addAttribute("aiReason", "BTC trend looks bullish. Market momentum is stable and risk remains manageable.");
            model.addAttribute("balance", String.format("$%.2f", balance));
            model.addAttribute("userRole", user.getRole());
            model.addAttribute("openTrades", openTrades);
            model.addAttribute("totalPnl", String.format("$%.2f", totalPnl));
            
            System.out.println("✅ Dashboard loaded successfully!");
            System.out.println("=========================================");
            
            return "dashboard";

        } catch (Exception e) {
            System.out.println("❌ Dashboard Error: " + e.getMessage());
            e.printStackTrace();
            return "redirect:/login";
        }
    }

    @ModelAttribute
    public void addUserDetails(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
            String email = auth.getName();
            User user = userService.getUserByEmail(email).orElse(null);
            if (user != null) {
                model.addAttribute("userRole", user.getRole());
                model.addAttribute("email", user.getEmail());
            }
        }
    }
    
    @GetMapping("/paper-trading")
    public String paperTrading(Model model) {
        try {
            System.out.println("=========================================");
            System.out.println("🔍 PAPER TRADING PAGE ACCESSED");
            
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            
            if (authentication == null || !authentication.isAuthenticated()) {
                System.out.println("❌ User NOT authenticated!");
                return "redirect:/login";
            }
            
            String email = authentication.getName();
            System.out.println("✅ Authenticated user: " + email);
            
            User user = userService.getUserByEmail(email).orElse(null);
            if (user == null) {
                System.out.println("❌ User NOT found!");
                return "redirect:/login";
            }
            
            if (!user.isEnabled()) {
                System.out.println("❌ User NOT verified!");
                return "redirect:/verify?email=" + email;
            }
            
            // ⭐ CHECK: PRO user with no payment
            if (user.getRole().equals("PRO") && !user.isPaymentDone()) {
                System.out.println("💎 PRO user - Payment pending! Redirecting to payment...");
                return "redirect:/payment/upgrade?email=" + email;
            }

            Double balance = user.getVirtualBalance();
            String btcPrice = marketDataService.getBTCPrice();
            double price = Double.parseDouble(btcPrice);
            TradingDecision decision = strategyService.analyzeMarket(price);

            PaperTrade paperTrade = new PaperTrade(
                decision.getSignal(),
                String.format("$%.2f", price),
                "$0.00",
                "Click BUY to start trading"
            );
            
            List<Trade> openTrades = paperTradingService.getOpenTrades();
            List<Trade> tradeHistory = paperTradingService.getTradeHistory();
            Double totalBTC = paperTradingService.getTotalBTC();
            Double totalPnl = paperTradingService.getTotalPnL();

            model.addAttribute("btcPrice", String.format("$%.2f", price));
            model.addAttribute("signal", decision.getSignal());
            model.addAttribute("paperTrade", paperTrade);
            model.addAttribute("balance", String.format("$%.2f", balance));
            model.addAttribute("userRole", user.getRole());
            model.addAttribute("openTrades", openTrades);
            model.addAttribute("tradeHistory", tradeHistory);
            model.addAttribute("totalBTC", String.format("%.6f", totalBTC));
            model.addAttribute("totalPnl", String.format("$%.2f", totalPnl));

            System.out.println("✅ Paper Trading loaded successfully!");
            System.out.println("=========================================");

            return "paper-trading";
        } catch (Exception e) {
            System.out.println("❌ Paper Trading Error: " + e.getMessage());
            e.printStackTrace();
            return "redirect:/login";
        }
    }
}