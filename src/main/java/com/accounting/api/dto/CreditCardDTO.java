package com.accounting.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreditCardDTO {
    private Long id;
    private String cardType;
    private String last4Digits;
    private Long companyId;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCardType() { return cardType; }
    public void setCardType(String cardType) { this.cardType = cardType; }
    public String getLast4Digits() { return last4Digits; }
    public void setLast4Digits(String last4Digits) { this.last4Digits = last4Digits; }
    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }

    public CreditCardDTO(Long id, String cardType, String last4Digits, Long companyId) {
        this.id = id;
        this.cardType = cardType;
        this.last4Digits = last4Digits;
        this.companyId = companyId;
    }
} 