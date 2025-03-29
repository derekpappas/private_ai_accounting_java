package com.accounting.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import com.accounting.api.dto.CompanyDTO;
import com.accounting.config.BaseIntegrationTest;
import com.accounting.model.entity.Company;
import com.accounting.model.entity.ContactPerson;
import com.accounting.model.enums.CorporationType;
import com.accounting.repository.CompanyRepository;
import com.accounting.repository.ContactPersonRepository;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional
class CompanyServiceIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private ContactPersonRepository contactPersonRepository;

    private ContactPerson contactPerson;
    private Company company;

    @BeforeEach
    void setUpTest() {
        // Create and save a contact person
        contactPerson = new ContactPerson();
        contactPerson.setName("John Doe");
        contactPerson.setEmail("john@example.com");
        contactPerson.setPhone("1234567890");
        contactPerson = contactPersonRepository.save(contactPerson);

        // Create and save a company with unique name
        company = new Company();
        company.setName("Initial Test Company");
        company.setCorporationType(CorporationType.LLC);
        company.setContactPerson(contactPerson);
        company = companyRepository.save(company);
    }

    private ContactPerson createContactPerson() {
        ContactPerson contactPerson = new ContactPerson();
        contactPerson.setName("John Doe");
        contactPerson.setEmail("john@example.com");
        contactPerson.setPhone("1234567890");
        return contactPersonRepository.save(contactPerson);
    }

    private CompanyDTO createTestCompanyDTO(Long contactPersonId) {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setName("Test Company " + System.currentTimeMillis()); // Make name unique
        companyDTO.setCorporationType(CorporationType.LLC);
        companyDTO.setContactPersonId(contactPersonId);
        return companyDTO;
    }

    @Test
    void createCompany_ValidInput_SavesCompany() {
        // Given
        ContactPerson contactPerson = createContactPerson();
        CompanyDTO companyDTO = createTestCompanyDTO(contactPerson.getId());

        // When
        CompanyDTO savedCompany = companyService.createCompany(companyDTO);

        // Then
        assertThat(savedCompany.getId()).isNotNull();
        assertThat(savedCompany.getName()).isEqualTo(companyDTO.getName());
        assertThat(savedCompany.getContactPersonId()).isEqualTo(contactPerson.getId());
    }

    @Test
    void createCompany_DuplicateName_ThrowsException() {
        // Create first company
        CompanyDTO company1 = new CompanyDTO();
        company1.setName("Test Company");
        company1.setCorporationType(CorporationType.LLC);
        companyService.createCompany(company1);

        // Try to create second company with same name
        CompanyDTO company2 = new CompanyDTO();
        company2.setName("Test Company");
        company2.setCorporationType(CorporationType.S_CORP);

        assertThrows(RuntimeException.class, () -> 
            companyService.createCompany(company2)
        );
    }

    @Test
    void getAllCompanies_ShouldReturnCompanyList() {
        // When
        var companies = companyService.getAllCompanies();

        // Then
        assertThat(companies).isNotEmpty();
        assertThat(companies.get(0).getId()).isEqualTo(company.getId());
        assertThat(companies.get(0).getName()).isEqualTo(company.getName());
        assertThat(companies.get(0).getCorporationType()).isEqualTo(company.getCorporationType());
        assertThat(companies.get(0).getContactPersonId()).isEqualTo(contactPerson.getId());
    }

    @Test
    void updateCompany_ValidInput_UpdatesCompany() {
        // Create initial company
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setName("Initial Company");
        companyDTO.setCorporationType(CorporationType.LLC);
        CompanyDTO savedCompany = companyService.createCompany(companyDTO);

        // Update company
        savedCompany.setName("Updated Company");
        savedCompany.setCorporationType(CorporationType.S_CORP);
        CompanyDTO updatedCompany = companyService.updateCompany(savedCompany);

        assertThat(updatedCompany.getId()).isEqualTo(savedCompany.getId());
        assertThat(updatedCompany.getName()).isEqualTo("Updated Company");
        assertThat(updatedCompany.getCorporationType()).isEqualTo(CorporationType.S_CORP);
    }

    @Test
    void getCompanyById_ExistingId_ReturnsCompany() {
        // Create company
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setName("Test Company");
        companyDTO.setCorporationType(CorporationType.LLC);
        CompanyDTO savedCompany = companyService.createCompany(companyDTO);

        // Retrieve company
        CompanyDTO retrievedCompany = companyService.getCompanyById(savedCompany.getId())
            .orElseThrow();

        assertThat(retrievedCompany.getId()).isEqualTo(savedCompany.getId());
        assertThat(retrievedCompany.getName()).isEqualTo("Test Company");
        assertThat(retrievedCompany.getCorporationType()).isEqualTo(CorporationType.LLC);
    }

    @Test
    void deleteCompany_ExistingId_RemovesCompany() {
        // Create company with unique name
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setName("Company To Delete " + System.currentTimeMillis());
        companyDTO.setCorporationType(CorporationType.LLC);
        CompanyDTO savedCompany = companyService.createCompany(companyDTO);

        // Delete company
        companyService.deleteCompany(savedCompany.getId());

        // Verify deletion
        assertThat(companyService.getCompanyById(savedCompany.getId())).isEmpty();
    }
} 