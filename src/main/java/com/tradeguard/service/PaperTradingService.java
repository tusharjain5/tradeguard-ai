package com.tradeguard.service;

import org.springframework.stereotype.Service;

import com.tradeguard.dto.PaperTrade;
import com.tradeguard.entity.Trade;
import com.tradeguard.entity.User;
import com.tradeguard.repository.TradeRepository;
import com.tradeguard.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;



@Service
public class PaperTradingService {
	  private final TradeRepository tradeRepository;
	    private final UserRepository userRepository;
	    private final MarketDataService marketDataService;

	    public PaperTradingService(TradeRepository tradeRepository,
	                               UserRepository userRepository,
	                               MarketDataService marketDataService) {
	        this.tradeRepository = tradeRepository;
	        this.userRepository = userRepository;
	        this.marketDataService = marketDataService;
	    }

	    public String getCurrentPrice() {
	        return marketDataService.getBTCPrice();
	    }

	    private User getCurrentUser() {
	        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	        String email = auth.getName();
	        return userRepository.findByEmail(email)
	                .orElseThrow(() -> new RuntimeException("User not found"));
	    }

	    public Double getUserBalance() {
	        User user = getCurrentUser();
	        return user.getVirtualBalance();
	    }

	    // ⭐ Total BTC = OPEN trades ka sum
	    public Double getTotalBTC() {
	        User user = getCurrentUser();
	        List<Trade> openTrades = tradeRepository.findByUserAndStatus(user, "OPEN");
	        double totalBTC = 0.0;
	        for (Trade trade : openTrades) {
	            if (trade.getSignal().equals("BUY")) {
	                totalBTC += trade.getQuantity();
	            }
	        }
	        return totalBTC;
	    }

	    // ⭐ Total PnL = All closed trades ka sum
	    public Double getTotalPnL() {
	        User user = getCurrentUser();
	        List<Trade> allTrades = tradeRepository.findByUserOrderByCreatedAtDesc(user);
	        double totalPnl = 0.0;
	        for (Trade trade : allTrades) {
	            if (trade.getPnl() != null) {
	                totalPnl += trade.getPnl();
	            }
	        }
	        return totalPnl;
	    }

	    // ⭐ BUY - Creates OPEN trade
	    @Transactional
	    public PaperTrade buyTrade(Double quantity) {
	        User user = getCurrentUser();
	        Double currentPrice = Double.parseDouble(marketDataService.getBTCPrice());

	        if (user.getRole().equals("FREE")) {
	            long openTrades = tradeRepository.findByUserAndStatus(user, "OPEN").size();
	            if (openTrades >= 3) {
	                return new PaperTrade("HOLD", String.format("$%.2f", currentPrice), "$0.00",
	                        "⚠️ Free limit: Max 3 open trades. Upgrade to PRO!");
	            }
	        }

	        Double cost = currentPrice * quantity;
	        Double balance = user.getVirtualBalance();
	        
	        if (cost > balance) {
	            return new PaperTrade("HOLD", String.format("$%.2f", currentPrice), "$0.00",
	                    "⚠️ Insufficient balance! Need $" + String.format("%.2f", cost));
	        }

	        Trade trade = new Trade();
	        trade.setUser(user);
	        trade.setSignal("BUY");
	        trade.setEntryPrice(currentPrice);
	        trade.setQuantity(quantity);
	        trade.setStatus("OPEN");
	        trade.setCreatedAt(LocalDateTime.now());
	        trade.setPnl(0.0);

	        user.setVirtualBalance(balance - cost);
	        userRepository.save(user);
	        tradeRepository.save(trade);

	        return new PaperTrade(
	                "BUY",
	                String.format("$%.2f", currentPrice),
	                String.format("$%.2f", 0.0),
	                "✅ " + quantity + " BTC bought at $" + String.format("%.2f", currentPrice)
	        );
	    }

	    // ⭐ SELL - Direct SELL from balance
	    @Transactional
	    public PaperTrade sellTrade(Double quantity) {
	        User user = getCurrentUser();
	        Double currentPrice = Double.parseDouble(marketDataService.getBTCPrice());
	        
	        Double totalBTC = getTotalBTC();
	        if (totalBTC < quantity) {
	            return new PaperTrade("HOLD", String.format("$%.2f", currentPrice), "$0.00",
	                    "⚠️ Insufficient BTC! You have " + String.format("%.6f", totalBTC) + " BTC");
	        }

	        List<Trade> openTrades = tradeRepository.findByUserAndStatus(user, "OPEN");
	        Double remainingToSell = quantity;
	        Double totalPnl = 0.0;
	        
	        for (Trade trade : openTrades) {
	            if (trade.getSignal().equals("BUY") && remainingToSell > 0) {
	                if (trade.getQuantity() <= remainingToSell) {
	                    // ⭐ FULL CLOSE
	                    remainingToSell -= trade.getQuantity();
	                    Double pnl = (currentPrice - trade.getEntryPrice()) * trade.getQuantity();
	                    totalPnl += pnl;
	                    trade.setExitPrice(currentPrice);
	                    trade.setStatus("CLOSED");
	                    trade.setClosedAt(LocalDateTime.now());
	                    trade.setPnl(pnl);
	                    tradeRepository.save(trade);
	                } else {
	                    // ⭐ PARTIAL SELL - Quantity update, status OPEN rahega
	                    Double soldQty = remainingToSell;
	                    Double pnl = (currentPrice - trade.getEntryPrice()) * soldQty;
	                    totalPnl += pnl;
	                    trade.setQuantity(trade.getQuantity() - soldQty);
	                    trade.setPnl(pnl);
	                    remainingToSell = 0.0;
	                    tradeRepository.save(trade);
	                }
	            }
	        }

	        // ⭐ Update balance
	        Double earnings = currentPrice * quantity;
	        Double balance = user.getVirtualBalance();
	        user.setVirtualBalance(balance + earnings);
	        userRepository.save(user);

	        return new PaperTrade(
	                "SELL",
	                String.format("$%.2f", currentPrice),
	                String.format("$%.2f", totalPnl),
	                "✅ " + quantity + " BTC sold at $" + String.format("%.2f", currentPrice)
	        );
	    }

	    public List<Trade> getOpenTrades() {
	        User user = getCurrentUser();
	        List<Trade> trades = tradeRepository.findByUserAndStatusOrderByCreatedAtDesc(user, "OPEN");
	        
	        Double currentPrice = Double.parseDouble(marketDataService.getBTCPrice());
	        for (Trade trade : trades) {
	            if (trade.getSignal().equals("BUY")) {
	                Double pnl = (currentPrice - trade.getEntryPrice()) * trade.getQuantity();
	                trade.setPnl(pnl);
	            }
	        }
	        return trades;
	    }

	    public List<Trade> getTradeHistory() {
	        User user = getCurrentUser();
	        return tradeRepository.findByUserOrderByCreatedAtDesc(user);
	    }
	}