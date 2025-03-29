package com.accounting.service.impl;

import com.accounting.api.dto.CreditCardDTO;
import com.accounting.api.mapper.CreditCardMapper;
import com.accounting.exception.AccountingException;
import com.accounting.model.entity.CreditCard;
import com.accounting.repository.CreditCardRepository;
import com.accounting.service.CreditCardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CreditCardServiceImpl implements CreditCardService {
    private final CreditCardRepository creditCardRepository;
    private final CreditCardMapper creditCardMapper;

    @Override
    public CreditCardDTO createCreditCard(CreditCardDTO creditCardDTO) {
        if (creditCardRepository.existsByCompanyIdAndLast4Digits(
                creditCardDTO.getCompanyId(), 
                creditCardDTO.getLast4Digits())) {
            throw new AccountingException("Credit card already exists for this company");
        }
        CreditCard creditCard = creditCardMapper.toEntity(creditCardDTO);
        CreditCard savedCreditCard = creditCardRepository.save(creditCard);
        return creditCardMapper.toDto(savedCreditCard);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CreditCardDTO> getCreditCardById(Long id) {
        return creditCardRepository.findById(id)
                .map(creditCardMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CreditCardDTO> getCreditCardsByCompanyId(Long companyId) {
        return creditCardMapper.toDtoList(
            creditCardRepository.findByCompanyId(companyId)
        );
    }

    @Override
    public CreditCardDTO updateCreditCard(CreditCardDTO creditCardDTO) {
        CreditCard existingCreditCard = creditCardRepository.findById(creditCardDTO.getId())
            .orElseThrow(() -> new AccountingException("Credit card not found with id: " + creditCardDTO.getId()));
            
        creditCardMapper.updateEntityFromDto(creditCardDTO, existingCreditCard);
        CreditCard updatedCreditCard = creditCardRepository.save(existingCreditCard);
        return creditCardMapper.toDto(updatedCreditCard);
    }

    @Override
    public void deleteCreditCard(Long id) {
        creditCardRepository.deleteById(id);
    }

    @Override
    public boolean existsByCompanyIdAndLast4Digits(Long companyId, String last4Digits) {
        return creditCardRepository.existsByCompanyIdAndLast4Digits(companyId, last4Digits);
    }
} 