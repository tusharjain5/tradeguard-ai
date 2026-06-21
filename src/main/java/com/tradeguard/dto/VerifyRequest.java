package com.tradeguard.dto;
import jakarta.validation.constraints.NotBlank;

public class VerifyRequest {

	@NotBlank(message = "Verification code is required")
    private String code;
    
    private String email;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
    
    // Getters and Setters
}