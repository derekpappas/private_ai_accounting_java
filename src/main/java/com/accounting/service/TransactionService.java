package com.accounting.service;

import com.accounting.api.dto.TransactionDTO;
import java.util.List;
import java.util.Optional;

public interface TransactionService {
    TransactionDTO createTransaction(TransactionDTO transactionDTO);
    Optional<TransactionDTO> getTransactionById(Long id);
    List<TransactionDTO> getTransactionsByFileId(Long fileId);
    List<TransactionDTO> getTransactionsByBankInfoId(Long bankInfoId);
    List<TransactionDTO> getTransactionsByCreditCardId(Long creditCardId);
    void deleteTransaction(Long id);
} 