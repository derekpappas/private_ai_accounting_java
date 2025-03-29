package com.accounting.repository;

import com.accounting.model.entity.BankInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BankInfoRepository extends JpaRepository<BankInfo, Long> {
    List<BankInfo> findByCompanyId(Long companyId);
    boolean existsByCompanyIdAndLast4Digits(Long companyId, String last4Digits);
} 