package com.accounting.api.dto;

import com.accounting.model.enums.CardType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class CreditCardDTO {
    private Long id;
    
    @NotNull(message = "Card type is required")
    private CardType cardType;
    
    @NotBlank(message = "Last 4 digits are required")
    @Pattern(regexp = "\\d{4}", message = "Last 4 digits must be exactly 4 digits")
    private String last4Digits;
    
    @NotNull(message = "Company ID is required")
    private Long companyId;
} 