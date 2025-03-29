package com.accounting.api.mapper;

import com.accounting.api.dto.TransactionDTO;
import com.accounting.model.entity.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    
    @Mapping(target = "file.id", source = "fileId")
    Transaction toEntity(TransactionDTO dto);

    @Mapping(source = "file.id", target = "fileId")
    TransactionDTO toDto(Transaction entity);

    List<TransactionDTO> toDtoList(List<Transaction> entities);

    void updateEntityFromDto(TransactionDTO dto, @MappingTarget Transaction entity);
} 