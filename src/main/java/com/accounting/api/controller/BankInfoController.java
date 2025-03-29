package com.accounting.api.controller;

import com.accounting.api.dto.BankInfoDTO;
import com.accounting.service.BankInfoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/bank-info")
@RequiredArgsConstructor
public class BankInfoController {
    private final BankInfoService bankInfoService;

    @PostMapping
    public ResponseEntity<BankInfoDTO> createBankInfo(@Valid @RequestBody BankInfoDTO bankInfoDTO) {
        return ResponseEntity.ok(bankInfoService.createBankInfo(bankInfoDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BankInfoDTO> getBankInfo(@PathVariable Long id) {
        return bankInfoService.getBankInfoById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<BankInfoDTO>> getBankInfoByCompany(@PathVariable Long companyId) {
        return ResponseEntity.ok(bankInfoService.getBankInfoByCompanyId(companyId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BankInfoDTO> updateBankInfo(
            @PathVariable Long id,
            @Valid @RequestBody BankInfoDTO bankInfoDTO) {
        bankInfoDTO.setId(id);
        return ResponseEntity.ok(bankInfoService.updateBankInfo(bankInfoDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBankInfo(@PathVariable Long id) {
        bankInfoService.deleteBankInfo(id);
        return ResponseEntity.noContent().build();
    }
} 