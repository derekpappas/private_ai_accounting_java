package com.accounting.service;

import com.accounting.api.dto.BankInfoDTO;
import com.accounting.config.BaseIntegrationTest;
import com.accounting.model.entity.Company;
import com.accounting.model.enums.AccountType;
import com.accounting.model.enums.CorporationType;
import com.accounting.repository.BankInfoRepository;
import com.accounting.repository.CompanyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BankInfoServiceIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private BankInfoService bankInfoService;

    @Autowired
    private BankInfoRepository bankInfoRepository;

    @Autowired
    private CompanyRepository companyRepository;

    private Company testCompany;

    @BeforeEach
    void setUp() {
        clearDatabase(); // Using inherited method from BaseIntegrationTest
        
        Company company = new Company();
        company.setName("Test Company");
        company.setCorporationType(CorporationType.LLC);
        testCompany = companyRepository.save(company);
    }

    @Test
    void createBankInfo_ValidInput_SavesBankInfo() {
        BankInfoDTO bankInfoDTO = new BankInfoDTO();
        bankInfoDTO.setBankName("Test Bank");
        bankInfoDTO.setAccountType(AccountType.CHECKING);
        bankInfoDTO.setLast4Digits("1234");
        bankInfoDTO.setCompanyId(testCompany.getId());

        BankInfoDTO savedBankInfo = bankInfoService.createBankInfo(bankInfoDTO);

        assertThat(savedBankInfo.getId()).isNotNull();
        assertThat(savedBankInfo.getBankName()).isEqualTo("Test Bank");
        assertThat(savedBankInfo.getAccountType()).isEqualTo(AccountType.CHECKING);
        assertThat(savedBankInfo.getLast4Digits()).isEqualTo("1234");
        assertThat(savedBankInfo.getCompanyId()).isEqualTo(testCompany.getId());
    }

    @Test
    void createBankInfo_DuplicateLast4Digits_ThrowsException() {
        // Create first bank info
        BankInfoDTO bankInfo1 = new BankInfoDTO();
        bankInfo1.setBankName("Test Bank 1");
        bankInfo1.setAccountType(AccountType.CHECKING);
        bankInfo1.setLast4Digits("1234");
        bankInfo1.setCompanyId(testCompany.getId());
        bankInfoService.createBankInfo(bankInfo1);

        // Try to create second bank info with same last 4 digits
        BankInfoDTO bankInfo2 = new BankInfoDTO();
        bankInfo2.setBankName("Test Bank 2");
        bankInfo2.setAccountType(AccountType.SAVINGS);
        bankInfo2.setLast4Digits("1234");
        bankInfo2.setCompanyId(testCompany.getId());

        assertThrows(RuntimeException.class, () -> 
            bankInfoService.createBankInfo(bankInfo2)
        );
    }

    @Test
    void getBankInfoByCompanyId_ExistingCompany_ReturnsList() {
        // Create first bank info
        BankInfoDTO bankInfo1 = new BankInfoDTO();
        bankInfo1.setBankName("Test Bank 1");
        bankInfo1.setAccountType(AccountType.CHECKING);
        bankInfo1.setLast4Digits("1234");
        bankInfo1.setCompanyId(testCompany.getId());
        bankInfoService.createBankInfo(bankInfo1);

        // Create second bank info
        BankInfoDTO bankInfo2 = new BankInfoDTO();
        bankInfo2.setBankName("Test Bank 2");
        bankInfo2.setAccountType(AccountType.SAVINGS);
        bankInfo2.setLast4Digits("5678");
        bankInfo2.setCompanyId(testCompany.getId());
        bankInfoService.createBankInfo(bankInfo2);

        // Retrieve bank infos
        List<BankInfoDTO> bankInfos = bankInfoService.getBankInfoByCompanyId(testCompany.getId());

        assertThat(bankInfos).hasSize(2);
        assertThat(bankInfos)
            .extracting(BankInfoDTO::getBankName)
            .containsExactlyInAnyOrder("Test Bank 1", "Test Bank 2");
        assertThat(bankInfos)
            .extracting(BankInfoDTO::getLast4Digits)
            .containsExactlyInAnyOrder("1234", "5678");
    }

    @Test
    void updateBankInfo_ValidInput_UpdatesBankInfo() {
        // Create initial bank info
        BankInfoDTO bankInfoDTO = new BankInfoDTO();
        bankInfoDTO.setBankName("Initial Bank");
        bankInfoDTO.setAccountType(AccountType.CHECKING);
        bankInfoDTO.setLast4Digits("1234");
        bankInfoDTO.setCompanyId(testCompany.getId());
        BankInfoDTO savedBankInfo = bankInfoService.createBankInfo(bankInfoDTO);

        // Update bank info
        savedBankInfo.setBankName("Updated Bank");
        savedBankInfo.setAccountType(AccountType.SAVINGS);
        BankInfoDTO updatedBankInfo = bankInfoService.updateBankInfo(savedBankInfo);

        assertThat(updatedBankInfo.getId()).isEqualTo(savedBankInfo.getId());
        assertThat(updatedBankInfo.getBankName()).isEqualTo("Updated Bank");
        assertThat(updatedBankInfo.getAccountType()).isEqualTo(AccountType.SAVINGS);
        assertThat(updatedBankInfo.getLast4Digits()).isEqualTo("1234");
        assertThat(updatedBankInfo.getCompanyId()).isEqualTo(testCompany.getId());
    }
} 