package com.tradeguard.controller;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.tradeguard.entity.Trade;
import com.tradeguard.entity.User;
import com.tradeguard.service.PaperTradingService;
import com.tradeguard.service.UserService;

import java.util.List;

@Controller
public class TradeHistoryController {

    private final PaperTradingService paperTradingService;
    private final UserService userService;

    public TradeHistoryController(PaperTradingService paperTradingService,
                                  UserService userService) {
        this.paperTradingService = paperTradingService;
        this.userService = userService;
    }


    @GetMapping("/trade-history")
    public String tradeHistory(Model model) {
        try {
            System.out.println("=========================================");
            System.out.println("🔍 TRADE HISTORY PAGE ACCESSED");
            
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            
            if (authentication == null || !authentication.isAuthenticated()) {
                System.out.println("❌ User not authenticated!");
                return "redirect:/login";
            }
            
            String email = authentication.getName();
            System.out.println("✅ Authenticated user: " + email);
            
            User user = userService.getUserByEmail(email).orElse(null);
            if (user == null) {
                System.out.println("❌ User not found!");
                return "redirect:/login";
            }

            // ⭐ FREE USER CHECK - Redirect to dashboard
            if (user.getRole().equals("FREE")) {
                System.out.println("❌ FREE user attempted to access Trade History page!");
                return "redirect:/dashboard";
            }

            List<Trade> tradeHistory = paperTradingService.getTradeHistory();
            System.out.println("📊 Total trades found: " + (tradeHistory != null ? tradeHistory.size() : 0));
            
            int totalTrades = tradeHistory != null ? tradeHistory.size() : 0;
            int winningTrades = 0;
            double totalPnl = 0.0;
            double maxDrawdown = 0.0;
            boolean hasDrawdown = false;
            
            if (tradeHistory != null) {
                for (Trade trade : tradeHistory) {
                    if (trade.getPnl() != null) {
                        totalPnl += trade.getPnl();
                        if (trade.getPnl() > 0) {
                            winningTrades++;
                        }
                        if (trade.getPnl() < maxDrawdown) {
                            maxDrawdown = trade.getPnl();
                            hasDrawdown = true;
                        }
                    }
                }
            }
            
            double winRate = totalTrades > 0 ? (double) winningTrades / totalTrades * 100 : 0;
            
            System.out.println("📊 Total Trades: " + totalTrades);
            System.out.println("📊 Winning Trades: " + winningTrades);
            System.out.println("📊 Win Rate: " + winRate + "%");
            System.out.println("📊 Total PnL: $" + totalPnl);
            
            model.addAttribute("tradeHistory", tradeHistory);
            model.addAttribute("userRole", user.getRole());
            model.addAttribute("userName", user.getFullName());
            model.addAttribute("totalTrades", totalTrades);
            model.addAttribute("winRate", String.format("%.0f%%", winRate));
            model.addAttribute("totalPnl", String.format("$%.2f", totalPnl));
            model.addAttribute("maxDrawdown", hasDrawdown ? String.format("$%.2f", maxDrawdown) : "$0.00");
            
            System.out.println("✅ Trade History loaded successfully!");
            System.out.println("=========================================");

            return "trade-history";
            
        } catch (Exception e) {
            System.out.println("❌ Trade History Error: " + e.getMessage());
            e.printStackTrace();
            return "redirect:/dashboard";
        }
    }
}