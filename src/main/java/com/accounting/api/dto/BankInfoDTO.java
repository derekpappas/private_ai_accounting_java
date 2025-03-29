package com.accounting.api.dto;

import com.accounting.model.enums.AccountType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class BankInfoDTO {
    private Long id;
    
    @NotBlank(message = "Bank name is required")
    private String bankName;
    
    @NotNull(message = "Account type is required")
    private AccountType accountType;
    
    @Pattern(regexp = "\\d{4}", message = "Last 4 digits must be exactly 4 digits")
    private String last4Digits;
    
    @NotNull(message = "Company ID is required")
    private Long companyId;
} 