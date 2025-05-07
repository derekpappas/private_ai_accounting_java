package com.accounting.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CompanyDTO {
    private Long id;
    private String name;
    private String corporationType;
    private Long contactPersonId;

    public Long getContactPersonId() {
        return contactPersonId;
    }

    public void setContactPersonId(Long contactPersonId) {
        this.contactPersonId = contactPersonId;
    }

    public CompanyDTO(Long id, String name, String corporationType, Long contactPersonId) {
        this.id = id;
        this.name = name;
        this.corporationType = corporationType;
        this.contactPersonId = contactPersonId;
    }
} 