package com.accounting.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BankInfoDTO {
    private Long id;
    private String bankName;
    private String accountNumber;
    private String routingNumber;
    private String accountType;
    private String last4Digits;
    private Long companyId;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getBankName() { return bankName; }
    public void setBankName(String bankName) { this.bankName = bankName; }
    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
    public String getRoutingNumber() { return routingNumber; }
    public void setRoutingNumber(String routingNumber) { this.routingNumber = routingNumber; }
    public String getAccountType() { return accountType; }
    public void setAccountType(String accountType) { this.accountType = accountType; }
    public String getLast4Digits() { return last4Digits; }
    public void setLast4Digits(String last4Digits) { this.last4Digits = last4Digits; }
    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }

    public BankInfoDTO(Long id, String bankName, String accountNumber, String routingNumber, String accountType, String last4Digits, Long companyId) {
        this.id = id;
        this.bankName = bankName;
        this.accountNumber = accountNumber;
        this.routingNumber = routingNumber;
        this.accountType = accountType;
        this.last4Digits = last4Digits;
        this.companyId = companyId;
    }
} 