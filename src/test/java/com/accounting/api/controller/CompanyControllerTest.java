package com.accounting.api.controller;

import com.accounting.api.dto.CompanyDTO;
import com.accounting.service.CompanyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CompanyControllerTest {

    @Mock
    private CompanyService companyService;

    @InjectMocks
    private CompanyController companyController;

    private CompanyDTO testCompanyDTO;

    @BeforeEach
    void setUp() {
        testCompanyDTO = new CompanyDTO();
        testCompanyDTO.setId(1L);
        testCompanyDTO.setName("Test Company");
        testCompanyDTO.setCorporationType("LLC");
    }

    @Test
    void getAllCompanies_ShouldReturnListOfCompanyDTOs() {
        // Arrange
        List<CompanyDTO> companyDTOs = Arrays.asList(testCompanyDTO);
        when(companyService.getAllCompanies()).thenReturn(companyDTOs);

        // Act
        ResponseEntity<List<CompanyDTO>> response = companyController.getAllCompanies();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(testCompanyDTO.getName(), response.getBody().get(0).getName());
    }

    @Test
    void getCompanyById_WhenExists_ShouldReturnCompanyDTO() {
        // Arrange
        when(companyService.getCompanyById(1L)).thenReturn(Optional.of(testCompanyDTO));

        // Act
        ResponseEntity<CompanyDTO> response = companyController.getCompanyById(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(testCompanyDTO.getName(), response.getBody().getName());
    }

    @Test
    void getCompanyById_WhenNotExists_ShouldReturnNotFound() {
        // Arrange
        when(companyService.getCompanyById(999L)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<CompanyDTO> response = companyController.getCompanyById(999L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void createCompany_ShouldReturnCreatedCompanyDTO() {
        // Arrange
        when(companyService.createCompany(testCompanyDTO)).thenReturn(testCompanyDTO);

        // Act
        ResponseEntity<CompanyDTO> response = companyController.createCompany(testCompanyDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(testCompanyDTO.getName(), response.getBody().getName());
    }

    @Test
    void updateCompany_ShouldReturnUpdatedCompanyDTO() {
        // Arrange
        CompanyDTO updatedCompanyDTO = new CompanyDTO();
        updatedCompanyDTO.setId(1L);
        updatedCompanyDTO.setName("Updated Company");
        updatedCompanyDTO.setCorporationType("Corp");

        when(companyService.updateCompany(updatedCompanyDTO)).thenReturn(updatedCompanyDTO);

        // Act
        ResponseEntity<CompanyDTO> response = companyController.updateCompany(1L, updatedCompanyDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(updatedCompanyDTO.getName(), response.getBody().getName());
    }

    @Test
    void deleteCompany_ShouldReturnNoContent() {
        // Arrange
        doNothing().when(companyService).deleteCompany(1L);

        // Act
        ResponseEntity<Void> response = companyController.deleteCompany(1L);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(companyService).deleteCompany(1L);
    }
} 