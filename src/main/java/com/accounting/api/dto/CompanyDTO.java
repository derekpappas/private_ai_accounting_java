package com.accounting.api.dto;

import lombok.Data;

@Data
public class CompanyDTO {
    private Long id;
    private String name;
    private String corporationType;
    private Long contactPersonId;
} 