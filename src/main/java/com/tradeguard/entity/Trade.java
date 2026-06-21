package com.tradeguard.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "trades")
public class Trade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(name = "trade_signal", nullable = false)
    private String signal;  // BUY or SELL
    
    @Column(name = "entry_price", nullable = false)
    private Double entryPrice;
    
    @Column(name = "exit_price")
    private Double exitPrice;
    
    @Column(nullable = false)
    private Double quantity;
    
    private Double pnl;
    
    @Column(nullable = false)
    private String status;  // OPEN, CLOSED
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "closed_at")
    private LocalDateTime closedAt;
    
    // ⭐ GETTERS AND SETTERS
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    
    public String getSignal() { return signal; }
    public void setSignal(String signal) { this.signal = signal; }
    
    public Double getEntryPrice() { return entryPrice; }
    public void setEntryPrice(Double entryPrice) { this.entryPrice = entryPrice; }
    
    public Double getExitPrice() { return exitPrice; }
    public void setExitPrice(Double exitPrice) { this.exitPrice = exitPrice; }
    
    public Double getQuantity() { return quantity; }
    public void setQuantity(Double quantity) { this.quantity = quantity; }
    
    public Double getPnl() { return pnl; }
    public void setPnl(Double pnl) { this.pnl = pnl; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getClosedAt() { return closedAt; }
    public void setClosedAt(LocalDateTime closedAt) { this.closedAt = closedAt; }
}
