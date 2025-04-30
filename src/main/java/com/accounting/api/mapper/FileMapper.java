package com.accounting.api.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.accounting.api.dto.FileDTO;
import com.accounting.model.entity.File;

@Mapper(componentModel = "spring")
public interface FileMapper {
    
    @Mapping(target = "bankInfo.id", source = "bankInfoId")
    @Mapping(target = "creditCard.id", source = "creditCardId")
    @Mapping(target = "transactions", ignore = true)
    @Mapping(target = "uploadedAt", expression = "java(java.time.LocalDateTime.now())")
    File toEntity(FileDTO dto);

    @Mapping(source = "bankInfo.id", target = "bankInfoId")
    @Mapping(source = "creditCard.id", target = "creditCardId")
    FileDTO toDto(File entity);

    List<FileDTO> toDtoList(List<File> entities);

    @Mapping(target = "uploadedAt", ignore = true)
    @Mapping(target = "bankInfo", ignore = true)
    @Mapping(target = "creditCard", ignore = true)
    @Mapping(target = "transactions", ignore = true)
    void updateEntityFromDto(FileDTO dto, @MappingTarget File entity);
} 