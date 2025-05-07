package com.accounting.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class FileDTO {
    private Long id;
    private String name;
    private String contentType;
    private String filePath;
    private LocalDateTime uploadedAt;
    private Long bankInfoId;
    private Long creditCardId;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getContentType() { return contentType; }
    public void setContentType(String contentType) { this.contentType = contentType; }
    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }
    public java.time.LocalDateTime getUploadedAt() { return uploadedAt; }
    public void setUploadedAt(java.time.LocalDateTime uploadedAt) { this.uploadedAt = uploadedAt; }
    public Long getBankInfoId() { return bankInfoId; }
    public void setBankInfoId(Long bankInfoId) { this.bankInfoId = bankInfoId; }
    public Long getCreditCardId() { return creditCardId; }
    public void setCreditCardId(Long creditCardId) { this.creditCardId = creditCardId; }

    public FileDTO(Long id, String name, String contentType, String filePath, java.time.LocalDateTime uploadedAt, Long bankInfoId, Long creditCardId) {
        this.id = id;
        this.name = name;
        this.contentType = contentType;
        this.filePath = filePath;
        this.uploadedAt = uploadedAt;
        this.bankInfoId = bankInfoId;
        this.creditCardId = creditCardId;
    }
} 