package com.accounting.repository;

import com.accounting.model.entity.BankInfo;
import com.accounting.model.entity.Company;
import com.accounting.model.entity.CreditCard;
import com.accounting.model.entity.File;
import com.accounting.model.enums.CardType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class FileRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private CreditCardRepository creditCardRepository;

    @Test
    void findByBankInfoId_ShouldReturnFiles() {
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
        entityManager.flush();

        // When
        List<File> found = fileRepository.findByBankInfoId(bankInfo.getId());

        // Then
        assertThat(found).hasSize(1);
        assertThat(found.get(0).getFilename()).isEqualTo("test.txt");
        assertThat(found.get(0).getType()).isEqualTo("text/plain");
        assertThat(found.get(0).getFilePath()).isEqualTo("/path/to/test.txt");
    }

    @Test
    void findByCreditCardId_ShouldReturnFiles() {
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
        entityManager.flush();

        // When
        List<File> found = fileRepository.findByCreditCardId(creditCard.getId());

        // Then
        assertThat(found).hasSize(1);
        assertThat(found.get(0).getFilename()).isEqualTo("test.txt");
        assertThat(found.get(0).getType()).isEqualTo("text/plain");
        assertThat(found.get(0).getFilePath()).isEqualTo("/path/to/test.txt");
    }

    @Test
    void save_ShouldPersistFile() {
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

        // When
        File saved = fileRepository.save(file);
        entityManager.flush();

        // Then
        File found = entityManager.find(File.class, saved.getId());
        assertThat(found).isNotNull();
        assertThat(found.getFilename()).isEqualTo("test.txt");
        assertThat(found.getType()).isEqualTo("text/plain");
        assertThat(found.getFilePath()).isEqualTo("/path/to/test.txt");
        assertThat(found.getBankInfo().getId()).isEqualTo(bankInfo.getId());
    }

    @Test
    void findById_WhenExists_ShouldReturnFile() {
        // Given
        File file = new File();
        file.setFilename("test.pdf");
        file.setType("application/pdf");
        file.setFilePath("/path/to/file");
        file.setUploadedAt(LocalDateTime.now());
        fileRepository.save(file);

        // When
        Optional<File> found = fileRepository.findById(file.getId());

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getFilename()).isEqualTo("test.pdf");
        assertThat(found.get().getType()).isEqualTo("application/pdf");
        assertThat(found.get().getFilePath()).isEqualTo("/path/to/file");
    }

    @Test
    void findById_WhenNotExists_ShouldReturnEmpty() {
        // When
        Optional<File> found = fileRepository.findById(999L);

        // Then
        assertThat(found).isEmpty();
    }

    @Test
    void save_WithCreditCard_ShouldSaveFileWithCreditCardReference() {
        // Given
        CreditCard creditCard = new CreditCard();
        creditCard.setCardType(CardType.VISA);
        creditCard.setLast4Digits("1234");
        creditCardRepository.save(creditCard);

        File file = new File();
        file.setFilename("statement.pdf");
        file.setType("application/pdf");
        file.setFilePath("/path/to/statement");
        file.setUploadedAt(LocalDateTime.now());
        file.setCreditCard(creditCard);

        // When
        File saved = fileRepository.save(file);

        // Then
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getCreditCard()).isNotNull();
        assertThat(saved.getCreditCard().getId()).isEqualTo(creditCard.getId());
    }
} 