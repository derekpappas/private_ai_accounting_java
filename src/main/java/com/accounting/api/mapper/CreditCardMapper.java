package com.accounting.api.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.accounting.api.dto.CreditCardDTO;
import com.accounting.model.entity.CreditCard;

@Mapper(componentModel = "spring")
public interface CreditCardMapper {
    
    @Mapping(target = "company.id", source = "companyId")
    @Mapping(target = "files", ignore = true)
    CreditCard toEntity(CreditCardDTO dto);

    @Mapping(source = "company.id", target = "companyId")
    CreditCardDTO toDto(CreditCard entity);

    List<CreditCardDTO> toDtoList(List<CreditCard> entities);

    @Mapping(target = "company", ignore = true)
    @Mapping(target = "files", ignore = true)
    void updateEntityFromDto(CreditCardDTO dto, @MappingTarget CreditCard entity);
} 