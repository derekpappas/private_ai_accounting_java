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
import com.accounting.model.enums.AccountType;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    uses = {Company.class}
)
public interface BankInfoMapper {
    
    @Mapping(target = "companyId", source = "company.id")
    BankInfoDTO toDto(BankInfo bankInfo);

    @Mapping(target = "company", source = "companyId")
    BankInfo toEntity(BankInfoDTO bankInfoDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "company", ignore = true)
    void updateEntityFromDto(BankInfoDTO bankInfoDTO, @MappingTarget BankInfo bankInfo);

    List<BankInfoDTO> toDtoList(List<BankInfo> bankInfos);

    default Company map(Long companyId) {
        if (companyId == null) {
            return null;
        }
        Company company = new Company();
        company.setId(companyId);
        return company;
    }

    default String accountTypeToString(AccountType accountType) {
        return accountType != null ? accountType.name() : null;
    }

    default AccountType stringToAccountType(String accountType) {
        return accountType != null ? AccountType.valueOf(accountType) : null;
    }
} 