package com.example.springtutorial.mappers;

import com.example.springtutorial.dto.InstanceDTO;
import com.example.springtutorial.entities.Instance;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {MetadataMapper.class})
public interface InstanceMapper {
    @Mapping(target = "metadata", source = ".")
    InstanceDTO  toDTO(Instance instance);
    List<InstanceDTO> toDTOs(List<Instance> instances);

    Instance toEntity(InstanceDTO instanceDTO);
}
