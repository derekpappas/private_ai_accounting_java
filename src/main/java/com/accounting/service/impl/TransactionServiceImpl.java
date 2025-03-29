package com.accounting.service.impl;

import com.accounting.api.dto.TransactionDTO;
import com.accounting.api.mapper.TransactionMapper;
import com.accounting.model.entity.Transaction;
import com.accounting.repository.TransactionRepository;
import com.accounting.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    @Override
    public TransactionDTO createTransaction(TransactionDTO transactionDTO) {
        Transaction transaction = transactionMapper.toEntity(transactionDTO);
        Transaction savedTransaction = transactionRepository.save(transaction);
        return transactionMapper.toDto(savedTransaction);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TransactionDTO> getTransactionById(Long id) {
        return transactionRepository.findById(id)
                .map(transactionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionDTO> getTransactionsByFileId(Long fileId) {
        return transactionMapper.toDtoList(
            transactionRepository.findByFileId(fileId)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionDTO> getTransactionsByBankInfoId(Long bankInfoId) {
        return transactionMapper.toDtoList(
            transactionRepository.findByFile_BankInfoId(bankInfoId)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionDTO> getTransactionsByCreditCardId(Long creditCardId) {
        return transactionMapper.toDtoList(
            transactionRepository.findByFile_CreditCardId(creditCardId)
        );
    }

    @Override
    public void deleteTransaction(Long id) {
        transactionRepository.deleteById(id);
    }
} 