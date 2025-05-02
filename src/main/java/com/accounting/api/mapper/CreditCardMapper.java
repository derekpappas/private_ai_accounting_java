package com.accounting.api.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.accounting.api.dto.CreditCardDTO;
import com.accounting.model.entity.CreditCard;
import com.accounting.model.entity.Company;
import com.accounting.model.enums.CardType;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface CreditCardMapper {
    
    @Mapping(target = "companyId", source = "company.id")
    @Mapping(target = "cardType", expression = "java(creditCard.getCardType() != null ? creditCard.getCardType().name() : null)")
    CreditCardDTO toDto(CreditCard creditCard);

    @Mapping(target = "company", expression = "java(map(dto.getCompanyId()))")
    @Mapping(target = "cardType", expression = "java(dto.getCardType() != null ? com.accounting.model.enums.CardType.valueOf(dto.getCardType()) : null)")
    @Mapping(target = "files", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    CreditCard toEntity(CreditCardDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "company", expression = "java(map(dto.getCompanyId()))")
    @Mapping(target = "files", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDto(CreditCardDTO dto, @MappingTarget CreditCard entity);

    List<CreditCardDTO> toDtoList(List<CreditCard> creditCards);

    default Company map(Long companyId) {
        if (companyId == null) return null;
        Company c = new Company();
        c.setId(companyId);
        return c;
    }

    default String cardTypeToString(CardType cardType) {
        return cardType != null ? cardType.name() : null;
    }

    default CardType stringToCardType(String cardType) {
        return cardType != null ? CardType.valueOf(cardType) : null;
    }
} 