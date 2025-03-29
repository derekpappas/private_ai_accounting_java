package com.accounting.api.mapper;

import com.accounting.api.dto.FileDTO;
import com.accounting.model.entity.File;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FileMapper {
    
    @Mapping(target = "bankInfo.id", source = "bankInfoId")
    @Mapping(target = "creditCard.id", source = "creditCardId")
    @Mapping(target = "transactions", ignore = true)
    File toEntity(FileDTO dto);

    @Mapping(source = "bankInfo.id", target = "bankInfoId")
    @Mapping(source = "creditCard.id", target = "creditCardId")
    FileDTO toDto(File entity);

    List<FileDTO> toDtoList(List<File> entities);

    void updateEntityFromDto(FileDTO dto, @MappingTarget File entity);
} 