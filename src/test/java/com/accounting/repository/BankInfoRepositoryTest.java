package com.accounting.repository;

import com.accounting.model.entity.BankInfo;
import com.accounting.model.entity.Company;
import com.accounting.model.enums.AccountType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class BankInfoRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BankInfoRepository bankInfoRepository;

    @Test
    void findByCompanyId_ShouldReturnBankInfos() {
        // Given
        Company company = new Company();
        company.setName("Test Company");
        company.setCorporationType("LLC");
        company = entityManager.persist(company);

        BankInfo bankInfo1 = new BankInfo();
        bankInfo1.setBankName("Bank 1");
        bankInfo1.setAccountNumber("1234");
        bankInfo1.setRoutingNumber("5678");
        bankInfo1.setLast4Digits("1234");
        bankInfo1.setAccountType(AccountType.CHECKING);
        bankInfo1.setCompany(company);
        entityManager.persist(bankInfo1);

        BankInfo bankInfo2 = new BankInfo();
        bankInfo2.setBankName("Bank 2");
        bankInfo2.setAccountNumber("5678");
        bankInfo2.setRoutingNumber("9012");
        bankInfo2.setLast4Digits("5678");
        bankInfo2.setAccountType(AccountType.SAVINGS);
        bankInfo2.setCompany(company);
        entityManager.persist(bankInfo2);

        entityManager.flush();

        // When
        List<BankInfo> found = bankInfoRepository.findByCompanyId(company.getId());

        // Then
        assertThat(found).hasSize(2);
        assertThat(found).extracting(BankInfo::getBankName)
                        .containsExactlyInAnyOrder("Bank 1", "Bank 2");
    }

    @Test
    void existsByCompanyIdAndLast4Digits_ShouldReturnTrue_WhenExists() {
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
        bankInfo.setAccountType(AccountType.CHECKING);
        bankInfo.setCompany(company);
        entityManager.persist(bankInfo);
        entityManager.flush();

        // When
        boolean exists = bankInfoRepository.existsByCompanyIdAndLast4Digits(
            company.getId(), "1234");

        // Then
        assertThat(exists).isTrue();
    }

    @Test
    void existsByCompanyIdAndLast4Digits_ShouldReturnFalse_WhenDoesNotExist() {
        // Given
        Company company = new Company();
        company.setName("Test Company");
        company.setCorporationType("LLC");
        company = entityManager.persist(company);
        entityManager.flush();

        // When
        boolean exists = bankInfoRepository.existsByCompanyIdAndLast4Digits(
            company.getId(), "1234");

        // Then
        assertThat(exists).isFalse();
    }

    @Test
    void save_ShouldPersistBankInfo() {
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
        bankInfo.setAccountType(AccountType.CHECKING);
        bankInfo.setCompany(company);

        // When
        BankInfo saved = bankInfoRepository.save(bankInfo);
        entityManager.flush();

        // Then
        BankInfo found = entityManager.find(BankInfo.class, saved.getId());
        assertThat(found).isNotNull();
        assertThat(found.getBankName()).isEqualTo("Test Bank");
        assertThat(found.getAccountNumber()).isEqualTo("1234");
        assertThat(found.getRoutingNumber()).isEqualTo("5678");
        assertThat(found.getLast4Digits()).isEqualTo("1234");
        assertThat(found.getAccountType()).isEqualTo(AccountType.CHECKING);
        assertThat(found.getCompany().getId()).isEqualTo(company.getId());
    }

    @Test
    void schemaValidation_shouldPersistAndRetrieveAllFields() {
        Company company = new Company();
        company.setName("Test Company");
        company.setCorporationType("LLC");
        entityManager.persist(company);

        BankInfo bankInfo = new BankInfo();
        bankInfo.setBankName("Test Bank");
        bankInfo.setAccountNumber("123456789");
        bankInfo.setRoutingNumber("987654321");
        bankInfo.setAccountType(AccountType.CHECKING);
        bankInfo.setLast4Digits("6789");
        bankInfo.setCompany(company);

        BankInfo saved = bankInfoRepository.save(bankInfo);
        entityManager.flush();
        entityManager.clear();

        Optional<BankInfo> found = bankInfoRepository.findById(saved.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getAccountNumber()).isEqualTo("123456789");
        assertThat(found.get().getBankName()).isEqualTo("Test Bank");
        assertThat(found.get().getRoutingNumber()).isEqualTo("987654321");
        assertThat(found.get().getAccountType()).isEqualTo(AccountType.CHECKING);
        assertThat(found.get().getLast4Digits()).isEqualTo("6789");
    }
} 