package com.tradeguard.service;


import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;


@Service
public class MarketDataService {

	
	 private final RestTemplate restTemplate =
	            new RestTemplate();

	    public String getBTCPrice() {

	        String url =
	                "https://api.bitget.com/api/v2/spot/market/tickers?symbol=BTCUSDT";

	        Map response =
	                restTemplate.getForObject(url, Map.class);

	        Map data =
	                ((java.util.List<Map>) response.get("data")).get(0);

	        return data.get("lastPr").toString();
	    }
	}
	
	

