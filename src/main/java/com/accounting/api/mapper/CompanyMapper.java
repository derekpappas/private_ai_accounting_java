package com.accounting.api.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.accounting.api.dto.CompanyDTO;
import com.accounting.model.entity.Company;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CompanyMapper {
    
    @Mapping(target = "contactPerson.id", source = "contactPersonId")
    @Mapping(target = "bankAccounts", ignore = true)
    @Mapping(target = "creditCards", ignore = true)
    Company toEntity(CompanyDTO dto);

    @Mapping(source = "contactPerson.id", target = "contactPersonId")
    CompanyDTO toDto(Company entity);

    List<CompanyDTO> toDtoList(List<Company> entities);

    @Mapping(target = "contactPerson", ignore = true)
    @Mapping(target = "bankAccounts", ignore = true)
    @Mapping(target = "creditCards", ignore = true)
    void updateEntityFromDto(CompanyDTO dto, @MappingTarget Company entity);
} 