package com.accounting.api.controller;

import com.accounting.api.dto.TransactionDTO;
import com.accounting.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<TransactionDTO> createTransaction(@Valid @RequestBody TransactionDTO transactionDTO) {
        return ResponseEntity.ok(transactionService.createTransaction(transactionDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionDTO> getTransaction(@PathVariable Long id) {
        return transactionService.getTransactionById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/file/{fileId}")
    public ResponseEntity<List<TransactionDTO>> getTransactionsByFile(@PathVariable Long fileId) {
        return ResponseEntity.ok(transactionService.getTransactionsByFileId(fileId));
    }

    @GetMapping("/bank-info/{bankInfoId}")
    public ResponseEntity<List<TransactionDTO>> getTransactionsByBankInfo(@PathVariable Long bankInfoId) {
        return ResponseEntity.ok(transactionService.getTransactionsByBankInfoId(bankInfoId));
    }

    @GetMapping("/credit-card/{creditCardId}")
    public ResponseEntity<List<TransactionDTO>> getTransactionsByCreditCard(@PathVariable Long creditCardId) {
        return ResponseEntity.ok(transactionService.getTransactionsByCreditCardId(creditCardId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }
} 