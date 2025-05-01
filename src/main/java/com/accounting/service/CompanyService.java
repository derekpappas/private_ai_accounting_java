package com.accounting.service;

import java.util.List;
import java.util.Optional;

import com.accounting.api.dto.CompanyDTO;
import com.accounting.model.entity.Company;

public interface CompanyService {
    List<CompanyDTO> getAllCompanies();
    Optional<CompanyDTO> getCompanyById(Long id);
    CompanyDTO createCompany(CompanyDTO companyDTO);
    CompanyDTO updateCompany(CompanyDTO companyDTO);
    void deleteCompany(Long id);
    boolean existsByName(String name);
    List<Company> findAll();
} 