package com.accounting.service;

import com.accounting.api.dto.CompanyDTO;
import com.accounting.api.mapper.CompanyMapper;
import com.accounting.model.entity.Company;
import com.accounting.repository.CompanyRepository;
import com.accounting.repository.ContactPersonRepository;
import com.accounting.service.impl.CompanyServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class CompanyServiceTest {

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private ContactPersonRepository contactPersonRepository;

    @Mock
    private CompanyMapper companyMapper;

    @InjectMocks
    private CompanyServiceImpl companyService;

    private Company testCompany;
    private CompanyDTO testCompanyDTO;

    @BeforeEach
    void setUp() {
        testCompany = new Company();
        testCompany.setId(1L);
        testCompany.setName("Test Company");
        testCompany.setCorporationType("LLC");

        testCompanyDTO = new CompanyDTO();
        testCompanyDTO.setId(1L);
        testCompanyDTO.setName("Test Company");
        testCompanyDTO.setCorporationType("LLC");

        // Setup default mapper behavior
        when(companyMapper.toDto(any(Company.class))).thenReturn(testCompanyDTO);
        when(companyMapper.toDtoList(anyList())).thenReturn(Arrays.asList(testCompanyDTO));
    }

    @Test
    void getAllCompanies_ShouldReturnListOfCompanyDTOs() {
        // Arrange
        when(companyRepository.findAll()).thenReturn(Arrays.asList(testCompany));

        // Act
        List<CompanyDTO> result = companyService.getAllCompanies();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testCompanyDTO.getName(), result.get(0).getName());
        verify(companyRepository).findAll();
        verify(companyMapper).toDtoList(Arrays.asList(testCompany));
    }

    @Test
    void getCompanyById_WhenExists_ShouldReturnCompanyDTO() {
        // Arrange
        when(companyRepository.findById(1L)).thenReturn(Optional.of(testCompany));

        // Act
        Optional<CompanyDTO> result = companyService.getCompanyById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(testCompanyDTO.getName(), result.get().getName());
        verify(companyRepository).findById(1L);
        verify(companyMapper).toDto(testCompany);
    }

    @Test
    void getCompanyById_WhenNotExists_ShouldReturnEmpty() {
        // Arrange
        when(companyRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        Optional<CompanyDTO> result = companyService.getCompanyById(999L);

        // Assert
        assertFalse(result.isPresent());
        verify(companyRepository).findById(999L);
        verify(companyMapper, never()).toDto(any());
    }

    @Test
    void createCompany_ShouldReturnSavedCompanyDTO() {
        // Arrange
        when(companyRepository.existsByName(testCompanyDTO.getName())).thenReturn(false);
        when(companyRepository.createCompanyWithEnumCast(
            testCompanyDTO.getName(),
            testCompanyDTO.getCorporationType(),
            testCompanyDTO.getContactPersonId()
        )).thenReturn(testCompany);

        // Act
        CompanyDTO result = companyService.createCompany(testCompanyDTO);

        // Assert
        assertNotNull(result);
        assertEquals(testCompanyDTO.getName(), result.getName());
        verify(companyRepository).existsByName(testCompanyDTO.getName());
        verify(companyRepository).createCompanyWithEnumCast(
            testCompanyDTO.getName(),
            testCompanyDTO.getCorporationType(),
            testCompanyDTO.getContactPersonId()
        );
        verify(companyMapper).toDto(testCompany);
    }

    @Test
    void updateCompany_ShouldReturnUpdatedCompanyDTO() {
        // Arrange
        when(companyRepository.findById(1L)).thenReturn(Optional.of(testCompany));
        when(companyRepository.save(any(Company.class))).thenReturn(testCompany);

        // Act
        CompanyDTO result = companyService.updateCompany(testCompanyDTO);

        // Assert
        assertNotNull(result);
        assertEquals(testCompanyDTO.getName(), result.getName());
        verify(companyRepository).findById(1L);
        verify(companyMapper).updateEntityFromDto(testCompanyDTO, testCompany);
        verify(companyRepository).save(testCompany);
        verify(companyMapper).toDto(testCompany);
    }

    @Test
    void deleteCompany_ShouldCallRepository() {
        // Arrange
        doNothing().when(companyRepository).deleteById(1L);

        // Act
        companyService.deleteCompany(1L);

        // Assert
        verify(companyRepository).deleteById(1L);
    }

    @Test
    void existsByName_WhenExists_ShouldReturnTrue() {
        // Arrange
        when(companyRepository.existsByName("Test Company")).thenReturn(true);

        // Act
        boolean result = companyService.existsByName("Test Company");

        // Assert
        assertTrue(result);
        verify(companyRepository).existsByName("Test Company");
    }

    @Test
    void existsByName_WhenNotExists_ShouldReturnFalse() {
        // Arrange
        when(companyRepository.existsByName("Non-existent Company")).thenReturn(false);

        // Act
        boolean result = companyService.existsByName("Non-existent Company");

        // Assert
        assertFalse(result);
        verify(companyRepository).existsByName("Non-existent Company");
    }
} 