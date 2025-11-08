package com.example.springtutorial.mappers;

import com.example.springtutorial.dto.InstanceDTO;
import com.example.springtutorial.dto.MetadataDTO;
import com.example.springtutorial.entities.Instance;
import com.example.springtutorial.entities.Metadata;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * A polymorphic dispatcher to handle instantiation of the abstract class {@link Metadata}
 * when inherited by multiple subclasses
 */
@Component
@RequiredArgsConstructor
public class MetadataMapperDispatcher {
    private final InstanceMapper instanceMapper;

    public MetadataDTO toDTO(Metadata metadata) {
        if (metadata instanceof Instance instance) {
            return instanceMapper.toDTO(instance);
        }
        throw new IllegalArgumentException("Unsupported metadata type: " + metadata.getClass().getName());
    }

    public Metadata toEntity(MetadataDTO metadataDTO) {
        if (metadataDTO instanceof InstanceDTO instanceDTO) {
            return instanceMapper.toEntity(instanceDTO);
        }
        throw new IllegalArgumentException("Unsupported metadata type: " + metadataDTO.getClass().getName());
    }
}
