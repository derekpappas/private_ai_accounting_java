package com.accounting.api.controller;

import com.accounting.api.dto.CreditCardDTO;
import com.accounting.exception.AccountingException;
import com.accounting.model.enums.CardType;
import com.accounting.service.CreditCardService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CreditCardController.class)
class CreditCardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CreditCardService creditCardService;

    @Test
    void createCreditCard_ValidInput_ReturnsCreated() throws Exception {
        CreditCardDTO dto = new CreditCardDTO();
        dto.setCardType(CardType.VISA);
        dto.setLast4Digits("1234");
        dto.setCompanyId(1L);

        when(creditCardService.createCreditCard(any())).thenReturn(dto);

        mockMvc.perform(post("/api/v1/credit-cards")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cardType").value("VISA"))
                .andExpect(jsonPath("$.last4Digits").value("1234"));
    }

    @Test
    void createCreditCard_InvalidInput_ReturnsBadRequest() throws Exception {
        CreditCardDTO dto = new CreditCardDTO();
        // Missing required fields

        mockMvc.perform(post("/api/v1/credit-cards")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").exists());
    }

    @Test
    void createCreditCard_DuplicateCard_ReturnsBadRequest() throws Exception {
        CreditCardDTO dto = new CreditCardDTO();
        dto.setCardType(CardType.VISA);
        dto.setLast4Digits("1234");
        dto.setCompanyId(1L);

        when(creditCardService.createCreditCard(any()))
            .thenThrow(new AccountingException("Credit card already exists for this company"));

        mockMvc.perform(post("/api/v1/credit-cards")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                    .value("Credit card already exists for this company"));
    }

    @Test
    void getCreditCardsByCompany_ValidCompanyId_ReturnsList() throws Exception {
        CreditCardDTO dto1 = new CreditCardDTO();
        dto1.setId(1L);
        dto1.setCardType(CardType.VISA);
        
        CreditCardDTO dto2 = new CreditCardDTO();
        dto2.setId(2L);
        dto2.setCardType(CardType.MASTERCARD);

        when(creditCardService.getCreditCardsByCompanyId(1L))
            .thenReturn(Arrays.asList(dto1, dto2));

        mockMvc.perform(get("/api/v1/credit-cards/company/{companyId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].cardType").value("VISA"))
                .andExpect(jsonPath("$[1].cardType").value("MASTERCARD"));
    }
} 