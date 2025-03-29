package com.accounting.api.mapper;

import com.accounting.api.dto.CompanyDTO;
import com.accounting.model.entity.Company;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CompanyMapper {
    
    @Mapping(target = "contactPerson.id", source = "contactPersonId")
    @Mapping(target = "bankAccounts", ignore = true)
    @Mapping(target = "creditCards", ignore = true)
    Company toEntity(CompanyDTO dto);

    @Mapping(source = "contactPerson.id", target = "contactPersonId")
    CompanyDTO toDto(Company entity);

    List<CompanyDTO> toDtoList(List<Company> entities);

    void updateEntityFromDto(CompanyDTO dto, @MappingTarget Company entity);
} 