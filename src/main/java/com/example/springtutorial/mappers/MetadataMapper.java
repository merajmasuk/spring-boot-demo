package com.example.springtutorial.mappers;

import com.example.springtutorial.dto.MetadataDTO;
import com.example.springtutorial.entities.Metadata;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {PersonMapper.class})
public interface MetadataMapper {
    @Mapping(target = "createdBy", source = "createdBy")
    @Mapping(target = "lastModifiedBy", source = "lastModifiedBy")
    @Mapping(target = "createdAt", expression = "java(metadata.getCreatedAt().toString())")
    @Mapping(target = "lastModifiedAt", expression = "java(metadata.getLastModifiedAt().toString())")
    MetadataDTO toDTO(Metadata metadata);

    Metadata toEntity(MetadataDTO metadataDTO);
}
