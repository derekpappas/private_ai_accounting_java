package com.accounting.service.pipeline.impl;

import com.accounting.exception.AccountingException;
import com.accounting.model.entity.File;
import com.accounting.service.pipeline.PipelineStage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
public class CSVReadingStage implements PipelineStage<File, CSVReadingStage.CSVContent> {

    @Override
    public CSVContent process(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file.getFilePath()))) {
            String headerLine = reader.readLine();
            if (headerLine == null) {
                throw new AccountingException("CSV file is empty");
            }

            List<String> headers = Arrays.asList(headerLine.split(","));
            List<List<String>> rows = new ArrayList<>();

            String line;
            while ((line = reader.readLine()) != null) {
                rows.add(Arrays.asList(line.split(",")));
            }

            log.info("Read CSV file: {} headers, {} rows", headers.size(), rows.size());
            return new CSVContent(headers, rows);

        } catch (Exception e) {
            throw new AccountingException("Failed to read CSV file: " + file.getFilename(), e);
        }
    }

    @lombok.Data
    @lombok.AllArgsConstructor
    public static class CSVContent {
        private List<String> headers;
        private List<List<String>> rows;
    }
} 