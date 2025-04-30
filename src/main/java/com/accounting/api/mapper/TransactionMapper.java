package com.accounting.api.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.accounting.api.dto.TransactionDTO;
import com.accounting.model.entity.Transaction;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    
    @Mapping(target = "file.id", source = "fileId")
    @Mapping(target = "additionalData", ignore = true)
    Transaction toEntity(TransactionDTO dto);

    @Mapping(source = "file.id", target = "fileId")
    TransactionDTO toDto(Transaction entity);

    List<TransactionDTO> toDtoList(List<Transaction> entities);

    @Mapping(target = "file", ignore = true)
    @Mapping(target = "additionalData", ignore = true)
    void updateEntityFromDto(TransactionDTO dto, @MappingTarget Transaction entity);
} 