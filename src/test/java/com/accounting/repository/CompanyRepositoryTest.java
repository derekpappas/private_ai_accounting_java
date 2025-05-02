package com.accounting.repository;

import com.accounting.model.entity.Company;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class CompanyRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CompanyRepository companyRepository;

    @Test
    void existsByName_ShouldReturnTrue_WhenExists() {
        // Given
        Company company = new Company();
        company.setName("Test Company");
        company.setCorporationType("LLC");
        entityManager.persist(company);
        entityManager.flush();

        // When
        boolean exists = companyRepository.existsByName("Test Company");

        // Then
        assertThat(exists).isTrue();
    }

    @Test
    void existsByName_ShouldReturnFalse_WhenDoesNotExist() {
        // When
        boolean exists = companyRepository.existsByName("Non-existent Company");

        // Then
        assertThat(exists).isFalse();
    }

    @Test
    void save_ShouldPersistCompany() {
        // Given
        Company company = new Company();
        company.setName("New Company");
        company.setCorporationType("LLC");

        // When
        Company saved = companyRepository.save(company);
        entityManager.flush();

        // Then
        Company found = entityManager.find(Company.class, saved.getId());
        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo("New Company");
        assertThat(found.getCorporationType()).isEqualTo("LLC");
    }

    @Test
    void createCompanyWithEnumCast_ShouldCreateCompany() {
        // When
        Company created = companyRepository.createCompanyWithEnumCast(
            "Test Company", "LLC", null);

        // Then
        assertThat(created).isNotNull();
        assertThat(created.getName()).isEqualTo("Test Company");
        assertThat(created.getCorporationType()).isEqualTo("LLC");
        assertThat(created.getId()).isNotNull();
    }
} 