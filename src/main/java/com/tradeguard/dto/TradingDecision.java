package com.tradeguard.dto;

public class TradingDecision {
	  private String signal;
	    private String confidence;
	    private String risk;

	    public TradingDecision(
	            String signal,
	            String confidence,
	            String risk
	    ) {
	        this.signal = signal;
	        this.confidence = confidence;
	        this.risk = risk;
	    }

	    public String getSignal() {
	        return signal;
	    }

	    public String getConfidence() {
	        return confidence;
	    }

	    public String getRisk() {
	        return risk;
	    }
	}
