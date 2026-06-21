package com.tradeguard.controller;


import org.springframework.web.bind.annotation.*;

import com.tradeguard.dto.PaperTrade;
import com.tradeguard.entity.Trade;
import com.tradeguard.service.MarketDataService;
import com.tradeguard.service.PaperTradingService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/paper-trade")
public class PaperTradingController {
	  private final PaperTradingService paperTradingService;
	    private final MarketDataService marketDataService;

	    public PaperTradingController(PaperTradingService paperTradingService,
	                                   MarketDataService marketDataService) {
	        this.paperTradingService = paperTradingService;
	        this.marketDataService = marketDataService;
	    }

	    @GetMapping("/price")
	    public Map<String, Object> getPrice() {
	        String price = marketDataService.getBTCPrice();
	        Map<String, Object> response = new HashMap<>();
	        response.put("price", price);
	        return response;
	    }

	    @PostMapping("/buy")
	    public Map<String, Object> buyTrade(@RequestBody Map<String, String> request) {
	        try {
	            Double quantity = Double.parseDouble(request.get("quantity"));
	            System.out.println("🟢 BUY Request: " + quantity + " BTC");
	            
	            PaperTrade trade = paperTradingService.buyTrade(quantity);

	            Map<String, Object> response = new HashMap<>();
	            response.put("entryPrice", trade.getEntryPrice());
	            response.put("status", trade.getStatus());
	            response.put("pnl", trade.getPnl());
	            response.put("signal", "BUY");
	            response.put("success", true);
	            response.put("message", trade.getStatus());
	            return response;
	        } catch (Exception e) {
	            Map<String, Object> response = new HashMap<>();
	            response.put("success", false);
	            response.put("error", e.getMessage());
	            return response;
	        }
	    }

	    @PostMapping("/sell")
	    public Map<String, Object> sellTrade(@RequestBody Map<String, String> request) {
	        try {
	            Double quantity = Double.parseDouble(request.get("quantity"));
	            System.out.println("🔴 SELL Request: " + quantity + " BTC");
	            
	            PaperTrade trade = paperTradingService.sellTrade(quantity);

	            Map<String, Object> response = new HashMap<>();
	            response.put("entryPrice", trade.getEntryPrice());
	            response.put("status", "COMPLETED");
	            response.put("pnl", trade.getPnl());
	            response.put("signal", "SELL");
	            response.put("success", true);
	            response.put("message", trade.getStatus());
	            return response;
	        } catch (Exception e) {
	            Map<String, Object> response = new HashMap<>();
	            response.put("success", false);
	            response.put("error", e.getMessage());
	            return response;
	        }
	    }

	    @GetMapping("/trades/open")
	    public List<Trade> getOpenTrades() {
	        return paperTradingService.getOpenTrades();
	    }

	    @GetMapping("/trades/history")
	    public List<Trade> getTradeHistory() {
	        return paperTradingService.getTradeHistory();
	    }

	    @GetMapping("/balance")
	    public Map<String, Object> getBalance() {
	        Double balance = paperTradingService.getUserBalance();
	        Map<String, Object> response = new HashMap<>();
	        response.put("balance", balance);
	        return response;
	    }

	    @GetMapping("/total-btc")
	    public Map<String, Object> getTotalBTC() {
	        Double totalBTC = paperTradingService.getTotalBTC();
	        Map<String, Object> response = new HashMap<>();
	        response.put("totalBTC", totalBTC);
	        return response;
	    }

	    // ⭐ NEW: Get Total PnL
	    @GetMapping("/total-pnl")
	    public Map<String, Object> getTotalPnL() {
	        Double totalPnl = paperTradingService.getTotalPnL();
	        Map<String, Object> response = new HashMap<>();
	        response.put("totalPnl", totalPnl);
	        return response;
	    }
	}