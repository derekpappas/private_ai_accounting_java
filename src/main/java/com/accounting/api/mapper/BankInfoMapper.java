package com.accounting.api.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.accounting.api.dto.BankInfoDTO;
import com.accounting.model.entity.BankInfo;

@Mapper(componentModel = "spring")
public interface BankInfoMapper {
    
    @Mapping(target = "companyId", source = "company.id")
    BankInfoDTO toDto(BankInfo bankInfo);

    @Mapping(target = "company.id", source = "companyId")
    @Mapping(target = "files", ignore = true)
    BankInfo toEntity(BankInfoDTO bankInfoDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "company.id", source = "companyId")
    @Mapping(target = "files", ignore = true)
    void updateEntityFromDto(BankInfoDTO bankInfoDTO, @MappingTarget BankInfo bankInfo);

    List<BankInfoDTO> toDtoList(List<BankInfo> bankInfos);
} 