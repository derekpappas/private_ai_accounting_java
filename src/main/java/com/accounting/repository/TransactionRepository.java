package com.accounting.repository;

import com.accounting.model.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByFileId(Long fileId);
    List<Transaction> findByFile_BankInfoId(Long bankInfoId);
    List<Transaction> findByFile_CreditCardId(Long creditCardId);
} 