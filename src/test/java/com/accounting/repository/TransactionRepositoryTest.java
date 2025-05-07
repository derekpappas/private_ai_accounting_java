package com.accounting.repository;

import com.accounting.model.entity.*;
import com.accounting.model.enums.CardType;
import com.accounting.model.enums.TransactionType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class TransactionRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private FileRepository fileRepository;

    @Test
    void findByFileId_ShouldReturnTransactions() {
        // Given
        Company company = new Company();
        company.setName("Test Company");
        company.setCorporationType("LLC");
        company = entityManager.persist(company);

        BankInfo bankInfo = new BankInfo();
        bankInfo.setBankName("Test Bank");
        bankInfo.setAccountNumber("1234");
        bankInfo.setRoutingNumber("5678");
        bankInfo.setLast4Digits("1234");
        bankInfo.setCompany(company);
        bankInfo = entityManager.persist(bankInfo);

        File file = new File();
        file.setFilename("test.txt");
        file.setType("text/plain");
        file.setFilePath("/path/to/test.txt");
        file.setUploadedAt(LocalDateTime.now());
        file.setBankInfo(bankInfo);
        file = entityManager.persist(file);

        Transaction transaction1 = new Transaction();
        transaction1.setTransactionType(TransactionType.DEBIT);
        transaction1.setDate(LocalDate.now());
        transaction1.setDescription("Transaction 1");
        transaction1.setAmount(new BigDecimal("100.00"));
        transaction1.setFile(file);
        entityManager.persist(transaction1);

        Transaction transaction2 = new Transaction();
        transaction2.setTransactionType(TransactionType.CREDIT);
        transaction2.setDate(LocalDate.now());
        transaction2.setDescription("Transaction 2");
        transaction2.setAmount(new BigDecimal("200.00"));
        transaction2.setFile(file);
        entityManager.persist(transaction2);

        entityManager.flush();

        // When
        List<Transaction> found = transactionRepository.findByFileId(file.getId());

        // Then
        assertThat(found).hasSize(2);
        assertThat(found).extracting(Transaction::getDescription)
                        .containsExactlyInAnyOrder("Transaction 1", "Transaction 2");
    }

    @Test
    void findByFile_BankInfoId_ShouldReturnTransactions() {
        // Given
        Company company = new Company();
        company.setName("Test Company");
        company.setCorporationType("LLC");
        company = entityManager.persist(company);

        BankInfo bankInfo = new BankInfo();
        bankInfo.setBankName("Test Bank");
        bankInfo.setAccountNumber("1234");
        bankInfo.setRoutingNumber("5678");
        bankInfo.setLast4Digits("1234");
        bankInfo.setCompany(company);
        bankInfo = entityManager.persist(bankInfo);

        File file = new File();
        file.setFilename("test.txt");
        file.setType("text/plain");
        file.setFilePath("/path/to/test.txt");
        file.setUploadedAt(LocalDateTime.now());
        file.setBankInfo(bankInfo);
        file = entityManager.persist(file);

        Transaction transaction = new Transaction();
        transaction.setTransactionType(TransactionType.DEBIT);
        transaction.setDate(LocalDate.now());
        transaction.setDescription("Bank Transaction");
        transaction.setAmount(new BigDecimal("100.00"));
        transaction.setFile(file);
        entityManager.persist(transaction);
        entityManager.flush();

        // When
        List<Transaction> found = transactionRepository.findByFile_BankInfoId(bankInfo.getId());

        // Then
        assertThat(found).hasSize(1);
        assertThat(found.get(0).getDescription()).isEqualTo("Bank Transaction");
    }

    @Test
    void findByFile_CreditCardId_ShouldReturnTransactions() {
        // Given
        Company company = new Company();
        company.setName("Test Company");
        company.setCorporationType("LLC");
        company = entityManager.persist(company);

        CreditCard creditCard = new CreditCard();
        creditCard.setCardType(CardType.VISA);
        creditCard.setLast4Digits("3456");
        creditCard.setCompany(company);
        creditCard = entityManager.persist(creditCard);

        File file = new File();
        file.setFilename("test.txt");
        file.setType("text/plain");
        file.setFilePath("/path/to/test.txt");
        file.setUploadedAt(LocalDateTime.now());
        file.setCreditCard(creditCard);
        file = entityManager.persist(file);

        Transaction transaction = new Transaction();
        transaction.setTransactionType(TransactionType.CREDIT);
        transaction.setDate(LocalDate.now());
        transaction.setDescription("Credit Card Transaction");
        transaction.setAmount(new BigDecimal("200.00"));
        transaction.setFile(file);
        entityManager.persist(transaction);
        entityManager.flush();

        // When
        List<Transaction> found = transactionRepository.findByFile_CreditCardId(creditCard.getId());

        // Then
        assertThat(found).hasSize(1);
        assertThat(found.get(0).getDescription()).isEqualTo("Credit Card Transaction");
    }

    @Test
    void findByFile_WhenExists_ShouldReturnTransactions() {
        // Given
        File file = new File();
        file.setFilename("statement.pdf");
        file.setType("application/pdf");
        file.setFilePath("/path/to/statement");
        file.setUploadedAt(LocalDateTime.now());
        fileRepository.save(file);

        Transaction transaction1 = new Transaction();
        transaction1.setTransactionType(TransactionType.DEBIT);
        transaction1.setDate(LocalDate.now());
        transaction1.setDescription("Transaction 1");
        transaction1.setAmount(new BigDecimal("100.00"));
        transaction1.setFile(file);
        transactionRepository.save(transaction1);

        Transaction transaction2 = new Transaction();
        transaction2.setTransactionType(TransactionType.CREDIT);
        transaction2.setDate(LocalDate.now());
        transaction2.setDescription("Transaction 2");
        transaction2.setAmount(new BigDecimal("200.00"));
        transaction2.setFile(file);
        transactionRepository.save(transaction2);

        // When
        List<Transaction> found = transactionRepository.findByFile(file);

        // Then
        assertThat(found).hasSize(2);
        assertThat(found).extracting("amount")
                .containsExactlyInAnyOrder(
                        new BigDecimal("100.00"),
                        new BigDecimal("200.00")
                );
    }

    @Test
    void findByFile_WhenNotExists_ShouldReturnEmptyList() {
        // Given
        File file = new File();
        file.setFilename("empty.pdf");
        file.setType("application/pdf");
        file.setFilePath("/path/to/empty");
        file.setUploadedAt(LocalDateTime.now());
        fileRepository.save(file);

        // When
        List<Transaction> found = transactionRepository.findByFile(file);

        // Then
        assertThat(found).isEmpty();
    }
} 