package com.accounting.api.controller;

import com.accounting.api.dto.FileDTO;
import com.accounting.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FileDTO> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(required = false) Long bankInfoId,
            @RequestParam(required = false) Long creditCardId) {
        return ResponseEntity.ok(fileService.uploadFile(file, bankInfoId, creditCardId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FileDTO> getFile(@PathVariable Long id) {
        return fileService.getFileById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFile(@PathVariable Long id) {
        fileService.deleteFile(id);
        return ResponseEntity.noContent().build();
    }
} 