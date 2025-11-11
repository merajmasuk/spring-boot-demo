package com.example.springtutorial.mappers;

import com.example.springtutorial.dto.InstanceDTO;
import com.example.springtutorial.entities.Instance;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", uses = {PersonMapper.class})
public interface InstanceMapper {
    InstanceMapper INSTANCE = Mappers.getMapper(InstanceMapper.class);

    @Mapping(target = "createdBy", source = "createdBy")
    @Mapping(target = "lastModifiedBy", source = "lastModifiedBy")
    @Mapping(target = "createdAt", expression = "java(instance.getCreatedAt().toString())")
    @Mapping(target = "lastModifiedAt", expression = "java(instance.getLastModifiedAt().toString())")
    InstanceDTO  toDTO(Instance instance);
    List<InstanceDTO> toDTOList(List<Instance> instances);

    Instance toEntity(InstanceDTO instanceDTO);
}
