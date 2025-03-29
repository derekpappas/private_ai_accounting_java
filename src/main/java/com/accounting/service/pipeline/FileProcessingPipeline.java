package com.accounting.service.pipeline;

import com.accounting.model.entity.File;
import org.springframework.web.multipart.MultipartFile;

public interface FileProcessingPipeline {
    File processFile(MultipartFile file);
} 