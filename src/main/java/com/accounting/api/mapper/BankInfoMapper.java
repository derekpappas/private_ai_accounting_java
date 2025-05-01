package com.accounting.api.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.BeanMapping;

import com.accounting.api.dto.BankInfoDTO;
import com.accounting.model.entity.BankInfo;
import com.accounting.model.entity.Company;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    uses = {Company.class}
)
public interface BankInfoMapper {
    
    @Mapping(target = "companyId", source = "company.id")
    BankInfoDTO toDto(BankInfo bankInfo);

    @Mapping(target = "company.id", source = "companyId")
    BankInfo toEntity(BankInfoDTO bankInfoDTO);

    @Mapping(target = "company.id", source = "companyId")
    void updateEntityFromDto(BankInfoDTO bankInfoDTO, @MappingTarget BankInfo bankInfo);

    List<BankInfoDTO> toDtoList(List<BankInfo> bankInfos);
} 