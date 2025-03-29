package com.accounting.service.pipeline.impl;

import com.accounting.exception.AccountingException;
import com.accounting.model.entity.File;
import com.accounting.service.pipeline.PipelineStage;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class PDFReadingStage implements PipelineStage<File, PDFReadingStage.PDFContent> {

    @Override
    public PDFContent process(File file) {
        try (PDDocument document = PDDocument.load(new java.io.File(file.getFilePath()))) {
            PDFTextStripper stripper = new PDFTextStripper();
            List<String> pages = new ArrayList<>();

            for (int i = 1; i <= document.getNumberOfPages(); i++) {
                stripper.setStartPage(i);
                stripper.setEndPage(i);
                String pageText = stripper.getText(document);
                pages.add(pageText);
            }

            log.info("Read PDF file: {} pages", pages.size());
            return new PDFContent(pages);

        } catch (Exception e) {
            throw new AccountingException("Failed to read PDF file: " + file.getFilename(), e);
        }
    }

    @lombok.Data
    @lombok.AllArgsConstructor
    public static class PDFContent {
        private List<String> pages;
    }
} 