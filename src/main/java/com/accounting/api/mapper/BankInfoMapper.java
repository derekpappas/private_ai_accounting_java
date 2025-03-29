package com.accounting.api.mapper;

import com.accounting.api.dto.BankInfoDTO;
import com.accounting.model.entity.BankInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BankInfoMapper {
    
    @Mapping(target = "company.id", source = "companyId")
    @Mapping(target = "files", ignore = true)
    BankInfo toEntity(BankInfoDTO dto);

    @Mapping(source = "company.id", target = "companyId")
    BankInfoDTO toDto(BankInfo entity);

    List<BankInfoDTO> toDtoList(List<BankInfo> entities);

    void updateEntityFromDto(BankInfoDTO dto, @MappingTarget BankInfo entity);
} 