package com.accounting.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FileDTO {
    private Long id;
    
    @NotBlank(message = "Filename is required")
    private String filename;
    
    @NotBlank(message = "File type is required")
    private String type;
    
    @NotBlank(message = "File path is required")
    private String filePath;
    
    private Long bankInfoId;
    private Long creditCardId;
} 