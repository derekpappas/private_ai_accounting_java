package com.accounting.api.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import com.accounting.api.dto.CompanyDTO;
import com.accounting.model.entity.Company;
import com.accounting.model.entity.ContactPerson;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface CompanyMapper {
    
    @Mapping(target = "contactPersonId", expression = "java(company.getContactPerson() != null ? company.getContactPerson().getId() : null)")
    CompanyDTO toDto(Company company);

    @Mapping(target = "contactPerson", expression = "java(map(companyDTO.getContactPersonId()))")
    Company toEntity(CompanyDTO companyDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "contactPerson", expression = "java(map(companyDTO.getContactPersonId()))")
    void updateEntityFromDto(CompanyDTO companyDTO, @MappingTarget Company company);

    List<CompanyDTO> toDtoList(List<Company> companies);

    default ContactPerson map(Long contactPersonId) {
        if (contactPersonId == null) {
            return null;
        }
        ContactPerson contactPerson = new ContactPerson();
        contactPerson.setId(contactPersonId);
        return contactPerson;
    }
} 