package com.accounting.service.impl;

import com.accounting.api.dto.FileDTO;
import com.accounting.api.mapper.FileMapper;
import com.accounting.exception.AccountingException;
import com.accounting.model.entity.File;
import com.accounting.repository.BankInfoRepository;
import com.accounting.repository.CreditCardRepository;
import com.accounting.repository.FileRepository;
import com.accounting.service.FileService;
import com.accounting.service.pipeline.FileProcessingPipeline;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
    private final FileRepository fileRepository;
    private final BankInfoRepository bankInfoRepository;
    private final CreditCardRepository creditCardRepository;
    private final FileProcessingPipeline fileProcessingPipeline;
    private final FileMapper fileMapper;

    @Override
    @Transactional
    public FileDTO uploadFile(MultipartFile file, Long bankInfoId, Long creditCardId) {
        if (bankInfoId == null && creditCardId == null) {
            throw new AccountingException("Either bank info or credit card must be specified");
        }

        File processedFile = fileProcessingPipeline.processFile(file);

        if (bankInfoId != null) {
            processedFile.setBankInfo(bankInfoRepository.findById(bankInfoId)
                .orElseThrow(() -> new AccountingException("Bank info not found")));
        } else {
            processedFile.setCreditCard(creditCardRepository.findById(creditCardId)
                .orElseThrow(() -> new AccountingException("Credit card not found")));
        }

        File savedFile = fileRepository.save(processedFile);
        return fileMapper.toDto(savedFile);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FileDTO> getFileById(Long id) {
        return fileRepository.findById(id)
                .map(fileMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FileDTO> getFilesByBankInfoId(Long bankInfoId) {
        return fileMapper.toDtoList(
            fileRepository.findByBankInfoId(bankInfoId)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<FileDTO> getFilesByCreditCardId(Long creditCardId) {
        return fileMapper.toDtoList(
            fileRepository.findByCreditCardId(creditCardId)
        );
    }

    @Override
    @Transactional
    public void deleteFile(Long id) {
        fileRepository.deleteById(id);
    }
} 