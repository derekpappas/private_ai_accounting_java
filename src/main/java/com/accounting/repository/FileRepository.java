package com.accounting.repository;

import com.accounting.model.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {
    List<File> findByBankInfoId(Long bankInfoId);
    List<File> findByCreditCardId(Long creditCardId);
} 