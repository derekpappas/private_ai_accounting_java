package com.accounting.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.accounting.api.dto.CompanyDTO;
import com.accounting.api.mapper.CompanyMapper;
import com.accounting.model.entity.Company;
import com.accounting.model.entity.ContactPerson;
import com.accounting.repository.CompanyRepository;
import com.accounting.repository.ContactPersonRepository;
import com.accounting.service.CompanyService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CompanyServiceImpl implements CompanyService {
    private final CompanyRepository companyRepository;
    private final ContactPersonRepository contactPersonRepository;
    private final CompanyMapper companyMapper;

    @Override
    @Transactional
    public CompanyDTO createCompany(CompanyDTO companyDTO) {
        if (companyRepository.existsByName(companyDTO.getName())) {
            throw new RuntimeException("Company with name " + companyDTO.getName() + " already exists");
        }

        Company company = companyMapper.toEntity(companyDTO);
        
        if (companyDTO.getContactPersonId() != null) {
            ContactPerson contactPerson = contactPersonRepository.findById(companyDTO.getContactPersonId())
                .orElseThrow(() -> new RuntimeException("Contact person not found"));
            company.setContactPerson(contactPerson);
        }

        return companyMapper.toDto(companyRepository.save(company));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CompanyDTO> getCompanyById(Long id) {
        return companyRepository.findById(id)
            .map(companyMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CompanyDTO> getAllCompanies() {
        return companyMapper.toDtoList(companyRepository.findAll());
    }

    @Override
    @Transactional
    public CompanyDTO updateCompany(CompanyDTO companyDTO) {
        Company company = companyRepository.findById(companyDTO.getId())
            .orElseThrow(() -> new RuntimeException("Company not found"));
        
        companyMapper.updateEntityFromDto(companyDTO, company);
        
        if (companyDTO.getContactPersonId() != null) {
            ContactPerson contactPerson = contactPersonRepository.findById(companyDTO.getContactPersonId())
                .orElseThrow(() -> new RuntimeException("Contact person not found"));
            company.setContactPerson(contactPerson);
        }

        return companyMapper.toDto(companyRepository.save(company));
    }

    @Override
    @Transactional
    public void deleteCompany(Long id) {
        companyRepository.deleteById(id);
    }

    @Override
    public boolean existsByName(String name) {
        return companyRepository.existsByName(name);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Company> findAll() {
        return companyRepository.findAll();
    }
} 