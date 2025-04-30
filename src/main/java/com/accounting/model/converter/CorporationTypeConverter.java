package com.accounting.model.converter;

import com.accounting.model.enums.CorporationType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class CorporationTypeConverter implements AttributeConverter<CorporationType, String> {

    @Override
    public String convertToDatabaseColumn(CorporationType corporationType) {
        if (corporationType == null) {
            return null;
        }
        return corporationType.name();
    }

    @Override
    public CorporationType convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return CorporationType.valueOf(dbData);
    }
} 