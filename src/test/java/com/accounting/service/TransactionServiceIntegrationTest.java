package com.accounting.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.accounting.api.dto.TransactionDTO;
import com.accounting.config.BaseIntegrationTest;
import com.accounting.model.entity.File;
import com.accounting.repository.FileRepository;
import com.accounting.repository.TransactionRepository;

class TransactionServiceIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private FileRepository fileRepository;

    private File testFile;

    @BeforeEach
    void setUp() {
        File file = new File();
        file.setFilename("test.pdf");
        file.setType("application/pdf");
        file.setFilePath("/path/to/test.pdf");
        file.setUploadedAt(LocalDateTime.now());
        
        testFile = fileRepository.save(file);
        flushAndClear();
    }

    @Test
    void createTransaction_ValidInput_SavesTransaction() {
        TransactionDTO dto = new TransactionDTO();
        dto.setDate(LocalDate.now());
        dto.setDescription("Test Transaction");
        dto.setAmount(new BigDecimal("100.00"));
        dto.setFileId(testFile.getId());

        TransactionDTO savedDto = transactionService.createTransaction(dto);

        assertThat(savedDto.getId()).isNotNull();
        assertThat(savedDto.getDescription()).isEqualTo("Test Transaction");
        assertThat(savedDto.getAmount()).isEqualByComparingTo(new BigDecimal("100.00"));
    }

    @Test
    void findByFileId_ExistingFile_ReturnsTransactions() {
        // Create first transaction DTO
        TransactionDTO transaction1 = new TransactionDTO();
        transaction1.setDate(LocalDate.now());
        transaction1.setDescription("Transaction 1");
        transaction1.setAmount(new BigDecimal("100.00"));
        transaction1.setFileId(testFile.getId());
        transactionService.createTransaction(transaction1);

        // Create second transaction DTO
        TransactionDTO transaction2 = new TransactionDTO();
        transaction2.setDate(LocalDate.now());
        transaction2.setDescription("Transaction 2");
        transaction2.setAmount(new BigDecimal("200.00"));
        transaction2.setFileId(testFile.getId());
        transactionService.createTransaction(transaction2);

        // Retrieve transactions using service method
        List<TransactionDTO> transactions = transactionService.getTransactionsByFileId(testFile.getId());

        assertThat(transactions).hasSize(2);
        assertThat(transactions)
            .extracting(TransactionDTO::getDescription)
            .containsExactlyInAnyOrder("Transaction 1", "Transaction 2");
        assertThat(transactions)
            .extracting(TransactionDTO::getAmount)
            .containsExactlyInAnyOrder(
                new BigDecimal("100.00"),
                new BigDecimal("200.00")
            );
    }

    @Test
    void getTransactionById_ExistingTransaction_ReturnsTransaction() {
        // Create and save transaction DTO
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setDate(LocalDate.now());
        transactionDTO.setDescription("Test Transaction");
        transactionDTO.setAmount(new BigDecimal("100.00"));
        transactionDTO.setFileId(testFile.getId());

        TransactionDTO savedTransaction = transactionService.createTransaction(transactionDTO);

        // Retrieve transaction
        TransactionDTO retrievedTransaction = transactionService.getTransactionById(savedTransaction.getId())
            .orElseThrow();

        assertThat(retrievedTransaction.getId()).isEqualTo(savedTransaction.getId());
        assertThat(retrievedTransaction.getDescription()).isEqualTo("Test Transaction");
        assertThat(retrievedTransaction.getAmount()).isEqualByComparingTo(new BigDecimal("100.00"));
        assertThat(retrievedTransaction.getFileId()).isEqualTo(testFile.getId());
    }
} 