package com.accounting.service;

import com.accounting.api.dto.BankInfoDTO;
import java.util.List;
import java.util.Optional;

public interface BankInfoService {
    BankInfoDTO createBankInfo(BankInfoDTO bankInfoDTO);
    Optional<BankInfoDTO> getBankInfoById(Long id);
    List<BankInfoDTO> getBankInfoByCompanyId(Long companyId);
    BankInfoDTO updateBankInfo(BankInfoDTO bankInfoDTO);
    void deleteBankInfo(Long id);
    boolean existsByCompanyIdAndLast4Digits(Long companyId, String last4Digits);
} 