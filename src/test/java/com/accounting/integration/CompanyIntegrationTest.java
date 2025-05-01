package com.accounting.integration;

import com.accounting.api.dto.CompanyDTO;
import com.accounting.model.entity.Company;
import com.accounting.repository.CompanyRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class CompanyIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Company testCompany;
    private CompanyDTO testCompanyDTO;

    @BeforeEach
    void setUp() {
        companyRepository.deleteAll();

        testCompany = new Company();
        testCompany.setName("Test Company");
        testCompany.setCorporationType("LLC");
        testCompany = companyRepository.save(testCompany);

        testCompanyDTO = new CompanyDTO();
        testCompanyDTO.setName("New Test Company");
        testCompanyDTO.setCorporationType("Corp");
    }

    @Test
    void getAllCompanies_ShouldReturnCompanyList() throws Exception {
        mockMvc.perform(get("/api/companies"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(testCompany.getName())))
                .andExpect(jsonPath("$[0].corporationType", is(testCompany.getCorporationType())));
    }

    @Test
    void getCompanyById_WhenExists_ShouldReturnCompany() throws Exception {
        mockMvc.perform(get("/api/companies/{id}", testCompany.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is(testCompany.getName())))
                .andExpect(jsonPath("$.corporationType", is(testCompany.getCorporationType())));
    }

    @Test
    void getCompanyById_WhenNotExists_ShouldReturn404() throws Exception {
        mockMvc.perform(get("/api/companies/{id}", 999))
                .andExpect(status().isNotFound());
    }

    @Test
    void createCompany_ShouldReturnCreatedCompany() throws Exception {
        mockMvc.perform(post("/api/companies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testCompanyDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is(testCompanyDTO.getName())))
                .andExpect(jsonPath("$.corporationType", is(testCompanyDTO.getCorporationType())));
    }

    @Test
    void updateCompany_WhenExists_ShouldReturnUpdatedCompany() throws Exception {
        mockMvc.perform(put("/api/companies/{id}", testCompany.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testCompanyDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is(testCompanyDTO.getName())))
                .andExpect(jsonPath("$.corporationType", is(testCompanyDTO.getCorporationType())));
    }

    @Test
    void updateCompany_WhenNotExists_ShouldReturn404() throws Exception {
        mockMvc.perform(put("/api/companies/{id}", 999)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testCompanyDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteCompany_WhenExists_ShouldReturn204() throws Exception {
        mockMvc.perform(delete("/api/companies/{id}", testCompany.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteCompany_WhenNotExists_ShouldReturn404() throws Exception {
        mockMvc.perform(delete("/api/companies/{id}", 999))
                .andExpect(status().isNotFound());
    }
} 