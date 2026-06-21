package com.tradeguard.dto;

public class PaperTrade {
	 private String tradeType;
	    private String entryPrice;
	    private String pnl;
	    private String status;

	    public PaperTrade(
	            String tradeType,
	            String entryPrice,
	            String pnl,
	            String status
	    ) {
	        this.tradeType = tradeType;
	        this.entryPrice = entryPrice;
	        this.pnl = pnl;
	        this.status = status;
	    }

	    public String getTradeType() {
	        return tradeType;
	    }

	    public String getEntryPrice() {
	        return entryPrice;
	    }

	    public String getPnl() {
	        return pnl;
	    }

	    public String getStatus() {
	        return status;
	    }
	}
