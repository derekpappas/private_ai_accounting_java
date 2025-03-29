package com.accounting.api.controller;

import com.accounting.api.dto.CompanyDTO;
import com.accounting.model.enums.CorporationType;
import com.accounting.service.CompanyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.Optional;

@WebMvcTest(CompanyController.class)
class CompanyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CompanyService companyService;

    @Test
    void createCompany_ValidInput_ReturnsCreated() throws Exception {
        CompanyDTO dto = new CompanyDTO();
        dto.setName("Test Company");
        dto.setCorporationType(CorporationType.LLC);

        when(companyService.createCompany(any())).thenReturn(dto);

        mockMvc.perform(post("/api/v1/companies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    void getCompany_ExistingId_ReturnsCompany() throws Exception {
        CompanyDTO dto = new CompanyDTO();
        dto.setId(1L);
        dto.setName("Test Company");
        dto.setCorporationType(CorporationType.LLC);

        when(companyService.getCompanyById(1L)).thenReturn(Optional.of(dto));

        mockMvc.perform(get("/api/v1/companies/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Company"))
                .andExpect(jsonPath("$.corporationType").value("LLC"));
    }

    @Test
    void getCompany_NonExistingId_ReturnsNotFound() throws Exception {
        when(companyService.getCompanyById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/companies/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateCompany_ValidInput_ReturnsUpdatedCompany() throws Exception {
        CompanyDTO dto = new CompanyDTO();
        dto.setId(1L);
        dto.setName("Updated Company");
        dto.setCorporationType(CorporationType.LLC);

        when(companyService.updateCompany(any())).thenReturn(dto);

        mockMvc.perform(put("/api/v1/companies/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Company"));
    }
} 