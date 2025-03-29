package com.accounting.service;

import com.accounting.api.dto.CreditCardDTO;
import java.util.List;
import java.util.Optional;

public interface CreditCardService {
    CreditCardDTO createCreditCard(CreditCardDTO creditCardDTO);
    Optional<CreditCardDTO> getCreditCardById(Long id);
    List<CreditCardDTO> getCreditCardsByCompanyId(Long companyId);
    CreditCardDTO updateCreditCard(CreditCardDTO creditCardDTO);
    void deleteCreditCard(Long id);
    boolean existsByCompanyIdAndLast4Digits(Long companyId, String last4Digits);
} 