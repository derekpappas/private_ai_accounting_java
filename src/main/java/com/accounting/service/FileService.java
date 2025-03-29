package com.accounting.service;

import com.accounting.api.dto.FileDTO;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Optional;

public interface FileService {
    FileDTO uploadFile(MultipartFile file, Long bankInfoId, Long creditCardId);
    Optional<FileDTO> getFileById(Long id);
    List<FileDTO> getFilesByBankInfoId(Long bankInfoId);
    List<FileDTO> getFilesByCreditCardId(Long creditCardId);
    void deleteFile(Long id);
} 