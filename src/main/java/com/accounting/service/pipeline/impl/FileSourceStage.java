package com.accounting.service.pipeline.impl;

import com.accounting.exception.AccountingException;
import com.accounting.model.entity.File;
import com.accounting.service.pipeline.PipelineStage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Component
public class FileSourceStage implements PipelineStage<MultipartFile, File> {
    private static final String UPLOAD_DIR = "uploads";

    @Override
    public File process(MultipartFile input) {
        try {
            Path uploadPath = Paths.get(UPLOAD_DIR);
            Files.createDirectories(uploadPath);

            String originalFilename = input.getOriginalFilename();
            String uniqueFilename = UUID.randomUUID() + "_" + originalFilename;
            Path filePath = uploadPath.resolve(uniqueFilename);

            input.transferTo(filePath.toFile());

            File file = new File();
            file.setFilename(originalFilename);
            file.setType(getFileType(originalFilename));
            file.setFilePath(filePath.toString());
            file.setUploadedAt(LocalDateTime.now());

            return file;
        } catch (Exception e) {
            throw new AccountingException("Failed to process uploaded file", e);
        }
    }

    private String getFileType(String filename) {
        return filename.substring(filename.lastIndexOf(".") + 1).toUpperCase();
    }
} 