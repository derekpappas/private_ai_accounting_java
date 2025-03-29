package com.accounting.api.dto;

import com.accounting.model.enums.CorporationType;

import lombok.Data;

@Data
public class CompanyDTO {
    private Long id;
    private String name;
    private CorporationType corporationType;
    private Long contactPersonId;
} 