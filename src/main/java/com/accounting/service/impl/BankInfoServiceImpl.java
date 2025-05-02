package com.accounting.service.impl;

import com.accounting.api.dto.BankInfoDTO;
import com.accounting.api.mapper.BankInfoMapper;
import com.accounting.exception.AccountingException;
import com.accounting.model.entity.BankInfo;
import com.accounting.repository.BankInfoRepository;
import com.accounting.service.BankInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BankInfoServiceImpl implements BankInfoService {
    private final BankInfoRepository bankInfoRepository;
    private final BankInfoMapper bankInfoMapper;

    @Autowired
    public BankInfoServiceImpl(BankInfoRepository bankInfoRepository, BankInfoMapper bankInfoMapper) {
        this.bankInfoRepository = bankInfoRepository;
        this.bankInfoMapper = bankInfoMapper;
    }

    @Override
    public BankInfoDTO createBankInfo(BankInfoDTO bankInfoDTO) {
        if (bankInfoRepository.existsByCompanyIdAndLast4Digits(
                bankInfoDTO.getCompanyId(), 
                bankInfoDTO.getLast4Digits())) {
            throw new AccountingException("Bank account already exists for this company");
        }
        BankInfo bankInfo = bankInfoMapper.toEntity(bankInfoDTO);
        BankInfo savedBankInfo = bankInfoRepository.save(bankInfo);
        return bankInfoMapper.toDto(savedBankInfo);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BankInfoDTO> getBankInfoById(Long id) {
        return bankInfoRepository.findById(id)
                .map(bankInfoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BankInfoDTO> getBankInfoByCompanyId(Long companyId) {
        return bankInfoMapper.toDtoList(
            bankInfoRepository.findByCompanyId(companyId)
        );
    }

    @Override
    public BankInfoDTO updateBankInfo(BankInfoDTO bankInfoDTO) {
        BankInfo existingBankInfo = bankInfoRepository.findById(bankInfoDTO.getId())
            .orElseThrow(() -> new AccountingException("Bank info not found with id: " + bankInfoDTO.getId()));
            
        bankInfoMapper.updateEntityFromDto(bankInfoDTO, existingBankInfo);
        BankInfo updatedBankInfo = bankInfoRepository.save(existingBankInfo);
        return bankInfoMapper.toDto(updatedBankInfo);
    }

    @Override
    public void deleteBankInfo(Long id) {
        bankInfoRepository.deleteById(id);
    }

    @Override
    public boolean existsByCompanyIdAndLast4Digits(Long companyId, String last4Digits) {
        return bankInfoRepository.existsByCompanyIdAndLast4Digits(companyId, last4Digits);
    }
} 