package com.tradeguard.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.tradeguard.dto.TradingDecision;

import java.util.List;
import java.util.Map;


@Service
public class StrategyService {

	 
	 private final RestTemplate restTemplate = new RestTemplate();

	    // Bitget se last 20 candles fetch karo
	    private List<Double> fetchClosingPrices() {
	        String url = "https://api.bitget.com/api/v2/spot/market/candles" +
	                     "?symbol=BTCUSDT&granularity=1min&limit=20";
	        Map response = restTemplate.getForObject(url, Map.class);
	        List<List<Object>> data = (List<List<Object>>) response.get("data");

	        List<Double> closes = new java.util.ArrayList<>();
	        for (List<Object> candle : data) {
	            // index 4 = close price
	            closes.add(Double.parseDouble(candle.get(4).toString()));
	        }
	        return closes;
	    }

	    // EMA calculate karo
	    private double calculateEMA(List<Double> prices, int period) {
	        double multiplier = 2.0 / (period + 1);
	        double ema = prices.get(0);
	        for (int i = 1; i < prices.size(); i++) {
	            ema = (prices.get(i) - ema) * multiplier + ema;
	        }
	        return ema;
	    }

	    // RSI calculate karo
	    private double calculateRSI(List<Double> prices) {
	        double gain = 0, loss = 0;
	        for (int i = 1; i < prices.size(); i++) {
	            double change = prices.get(i) - prices.get(i - 1);
	            if (change > 0) gain += change;
	            else loss += Math.abs(change);
	        }
	        if (loss == 0) return 100;
	        double rs = gain / loss;
	        return 100 - (100 / (1 + rs));
	    }

	    public TradingDecision analyzeMarket(double price) {
	        try {
	            List<Double> closes = fetchClosingPrices();

	            double ema9  = calculateEMA(closes, 9);
	            double ema21 = calculateEMA(closes, 21);
	            double rsi   = calculateRSI(closes);

	            String signal;
	            String confidence;
	            String risk;

	            // BUY: EMA9 > EMA21 (bullish) aur RSI < 70 (not overbought)
	            if (ema9 > ema21 && rsi < 70) {
	                signal = "BUY";
	                confidence = String.format("%.0f%%", 60 + (70 - rsi) * 0.5);
	                risk = rsi < 50 ? "LOW" : "MEDIUM";
	            }
	            // SELL: EMA9 < EMA21 (bearish) aur RSI > 30 (not oversold)
	            else if (ema9 < ema21 && rsi > 30) {
	                signal = "SELL";
	                confidence = String.format("%.0f%%", 60 + (rsi - 30) * 0.5);
	                risk = rsi > 70 ? "HIGH" : "MEDIUM";
	            }
	            // HOLD: sideways market
	            else {
	                signal = "HOLD";
	                confidence = "55%";
	                risk = "MEDIUM";
	            }

	            return new TradingDecision(signal, confidence, risk);

	        } catch (Exception e) {
	            // fallback agar API fail ho
	            return new TradingDecision("HOLD", "50%", "MEDIUM");
	        }
	    }
	}
	 
