package com.accounting.api.controller;

import com.accounting.AccountingApplication;
import com.accounting.api.dto.BankInfoDTO;
import com.accounting.exception.AccountingException;
import com.accounting.model.enums.AccountType;
import com.accounting.service.BankInfoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BankInfoController.class)
@ContextConfiguration(classes = AccountingApplication.class)
class BankInfoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BankInfoService bankInfoService;

    @Test
    void createBankInfo_ValidInput_ReturnsCreated() throws Exception {
        BankInfoDTO dto = new BankInfoDTO();
        dto.setBankName("Test Bank");
        dto.setAccountType(AccountType.CHECKING);
        dto.setLast4Digits("1234");
        dto.setCompanyId(1L);

        when(bankInfoService.createBankInfo(any())).thenReturn(dto);

        mockMvc.perform(post("/api/v1/bank-info")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bankName").value("Test Bank"))
                .andExpect(jsonPath("$.accountType").value("CHECKING"));
    }

    @Test
    void getBankInfo_ExistingId_ReturnsBankInfo() throws Exception {
        BankInfoDTO dto = new BankInfoDTO();
        dto.setId(1L);
        dto.setBankName("Test Bank");
        dto.setAccountType(AccountType.CHECKING);

        when(bankInfoService.getBankInfoById(1L)).thenReturn(Optional.of(dto));

        mockMvc.perform(get("/api/v1/bank-info/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.bankName").value("Test Bank"));
    }

    @Test
    void createBankInfo_InvalidInput_ReturnsBadRequest() throws Exception {
        BankInfoDTO dto = new BankInfoDTO();
        // Missing required fields

        mockMvc.perform(post("/api/v1/bank-info")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").exists());
    }

    @Test
    void createBankInfo_DuplicateAccount_ReturnsBadRequest() throws Exception {
        BankInfoDTO dto = new BankInfoDTO();
        dto.setBankName("Test Bank");
        dto.setAccountType(AccountType.CHECKING);
        dto.setLast4Digits("1234");
        dto.setCompanyId(1L);

        when(bankInfoService.createBankInfo(any()))
            .thenThrow(new AccountingException("Bank account already exists for this company"));

        mockMvc.perform(post("/api/v1/bank-info")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                    .value("Bank account already exists for this company"));
    }

    @Test
    void getBankInfo_NonExistingId_ReturnsNotFound() throws Exception {
        when(bankInfoService.getBankInfoById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/bank-info/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateBankInfo_ValidInput_ReturnsUpdatedBankInfo() throws Exception {
        BankInfoDTO dto = new BankInfoDTO();
        dto.setId(1L);
        dto.setBankName("Updated Bank");
        dto.setAccountType(AccountType.SAVINGS);
        dto.setLast4Digits("5678");
        dto.setCompanyId(1L);

        when(bankInfoService.updateBankInfo(any())).thenReturn(dto);

        mockMvc.perform(put("/api/v1/bank-info/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bankName").value("Updated Bank"))
                .andExpect(jsonPath("$.accountType").value("SAVINGS"));
    }

    @Test
    void deleteBankInfo_ExistingId_ReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/v1/bank-info/{id}", 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteBankInfo_NonExistingId_ReturnsBadRequest() throws Exception {
        doThrow(new AccountingException("Bank info not found"))
            .when(bankInfoService).deleteBankInfo(1L);

        mockMvc.perform(delete("/api/v1/bank-info/{id}", 1L))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Bank info not found"));
    }

    @Test
    void getBankInfoByCompany_ValidCompanyId_ReturnsList() throws Exception {
        BankInfoDTO dto1 = new BankInfoDTO();
        dto1.setId(1L);
        dto1.setBankName("Bank 1");
        
        BankInfoDTO dto2 = new BankInfoDTO();
        dto2.setId(2L);
        dto2.setBankName("Bank 2");

        when(bankInfoService.getBankInfoByCompanyId(1L))
            .thenReturn(Arrays.asList(dto1, dto2));

        mockMvc.perform(get("/api/v1/bank-info/company/{companyId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].bankName").value("Bank 1"))
                .andExpect(jsonPath("$[1].bankName").value("Bank 2"));
    }
} 