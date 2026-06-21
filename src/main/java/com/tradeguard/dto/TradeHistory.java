package com.tradeguard.dto;

public class TradeHistory {
	 private String time;
	    private String signal;
	    private String entryPrice;
	    private String status;

	    public TradeHistory(
	            String time,
	            String signal,
	            String entryPrice,
	            String status
	    ) {
	        this.time = time;
	        this.signal = signal;
	        this.entryPrice = entryPrice;
	        this.status = status;
	    }

	    public String getTime() {
	        return time;
	    }

	    public String getSignal() {
	        return signal;
	    }

	    public String getEntryPrice() {
	        return entryPrice;
	    }

	    public String getStatus() {
	        return status;
	    }
	}
