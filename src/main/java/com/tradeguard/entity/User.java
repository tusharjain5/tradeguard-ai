package com.tradeguard.entity;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {
	  @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;
	    
	    @Column(unique = true, nullable = false)
	    private String email;
	    
	    @Column(nullable = false)
	    private String password;
	    
	    @Column(nullable = false)
	    private String fullName;
	    
	    @Column(nullable = false)
	    private String role = "FREE";  // FREE or PRO
	    
	    private String phoneNumber;
	    private String country;
	    
	    @Column(nullable = false)
	    private boolean enabled = false;
	    
	    private String verificationCode;
	    private LocalDateTime verificationCodeExpiry;
	    
	    @Column(nullable = false)
	    private Double virtualBalance = 1000.0;  // FREE: $1,000
	    
	    // ⭐ NEW: Payment Done Flag
	    @Column(nullable = false)
	    private boolean paymentDone = false;
	    
	    private LocalDateTime createdAt;
	    private LocalDateTime updatedAt;
	    
	    // ⭐ UPGRADE TO PRO METHOD - WITH PAYMENT
	    public void upgradeToPro() {
	        this.role = "PRO";
	        this.virtualBalance = 10000.0;  // PRO: $10,000
	        this.paymentDone = true;
	        this.updatedAt = LocalDateTime.now();
	    }
	    
	    // ⭐ GETTERS AND SETTERS
	    public Long getId() { return id; }
	    public void setId(Long id) { this.id = id; }
	    
	    public String getEmail() { return email; }
	    public void setEmail(String email) { this.email = email; }
	    
	    public String getPassword() { return password; }
	    public void setPassword(String password) { this.password = password; }
	    
	    public String getFullName() { return fullName; }
	    public void setFullName(String fullName) { this.fullName = fullName; }
	    
	    public String getRole() { return role; }
	    public void setRole(String role) { this.role = role; }
	    
	    public String getPhoneNumber() { return phoneNumber; }
	    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
	    
	    public String getCountry() { return country; }
	    public void setCountry(String country) { this.country = country; }
	    
	    public boolean isEnabled() { return enabled; }
	    public void setEnabled(boolean enabled) { this.enabled = enabled; }
	    
	    public String getVerificationCode() { return verificationCode; }
	    public void setVerificationCode(String verificationCode) { this.verificationCode = verificationCode; }
	    
	    public LocalDateTime getVerificationCodeExpiry() { return verificationCodeExpiry; }
	    public void setVerificationCodeExpiry(LocalDateTime verificationCodeExpiry) { this.verificationCodeExpiry = verificationCodeExpiry; }
	    
	    public Double getVirtualBalance() { return virtualBalance; }
	    public void setVirtualBalance(Double virtualBalance) { this.virtualBalance = virtualBalance; }
	    
	    public boolean isPaymentDone() { return paymentDone; }
	    public void setPaymentDone(boolean paymentDone) { this.paymentDone = paymentDone; }
	    
	    public LocalDateTime getCreatedAt() { return createdAt; }
	    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
	    
	    public LocalDateTime getUpdatedAt() { return updatedAt; }
	    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
	}