package com.accounting.api.controller;

import com.accounting.api.dto.CreditCardDTO;
import com.accounting.service.CreditCardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/credit-cards")
@RequiredArgsConstructor
public class CreditCardController {
    private final CreditCardService creditCardService;

    @PostMapping
    public ResponseEntity<CreditCardDTO> createCreditCard(@Valid @RequestBody CreditCardDTO creditCardDTO) {
        return ResponseEntity.ok(creditCardService.createCreditCard(creditCardDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CreditCardDTO> getCreditCard(@PathVariable Long id) {
        return creditCardService.getCreditCardById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<CreditCardDTO>> getCreditCardsByCompany(@PathVariable Long companyId) {
        return ResponseEntity.ok(creditCardService.getCreditCardsByCompanyId(companyId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CreditCardDTO> updateCreditCard(
            @PathVariable Long id,
            @Valid @RequestBody CreditCardDTO creditCardDTO) {
        creditCardDTO.setId(id);
        return ResponseEntity.ok(creditCardService.updateCreditCard(creditCardDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCreditCard(@PathVariable Long id) {
        creditCardService.deleteCreditCard(id);
        return ResponseEntity.noContent().build();
    }
} 