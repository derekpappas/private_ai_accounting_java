package com.accounting.service.pipeline.impl;

import com.accounting.model.entity.File;
import com.accounting.service.pipeline.FileProcessingPipeline;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class FileProcessingPipelineImpl implements FileProcessingPipeline {
    private final FileSourceStage fileSourceStage;
    private final CSVReadingStage csvReadingStage;
    private final PDFReadingStage pdfReadingStage;

    @Override
    public File processFile(MultipartFile file) {
        // First stage: Save file and create File entity
        return fileSourceStage.process(file);
    }
} 