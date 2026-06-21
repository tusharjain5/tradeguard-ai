package com.tradeguard.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tradeguard.service.MarketDataService;

import java.util.HashMap;
import java.util.Map;


@RestController
public class MarketController {

	 private final MarketDataService marketDataService;

	    public MarketController(MarketDataService marketDataService) {
	        this.marketDataService = marketDataService;
	    }

	    @GetMapping("/api/btc-price")
	    public Map<String, String> getBTCPrice() {

	        String price =
	                marketDataService.getBTCPrice();

	        Map<String, String> response =
	                new HashMap<>();

	        response.put("coin", "BTCUSDT");
	        response.put("price", price);

	        return response;
	    }
	}
	

