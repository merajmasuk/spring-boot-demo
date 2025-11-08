package com.example.springtutorial;

import com.example.springtutorial.dao.InstanceDAO;
import com.example.springtutorial.dto.InstanceDTO;
import com.example.springtutorial.entities.Instance;
import com.example.springtutorial.exceptions.BusinessException;
import com.example.springtutorial.mappers.InstanceMapper;
import com.example.springtutorial.repository.InstanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InstanceService {

    private final InstanceRepository instanceRepository;
    private final InstanceMapper instanceMapper;

    public InstanceDTO createInstance(InstanceDAO request) throws BusinessException {
        Optional<Instance> instanceOptional = instanceRepository.findByTitleAndIsDeleted(request.getTitle(), false);
        if (instanceOptional.isPresent()) {
            throw new BusinessException("INSTANCE_ALREADY_EXISTS", "The instance you trying to create already exists!");
        }
        Instance instance = Instance.builder()
                .id(UUID.randomUUID())
                .title(request.getTitle())
                .description(request.getDescription())
                .isDeleted(false)
                .build();
        instanceRepository.save(instance);
        return instanceMapper.toDTO(instance);
    }
}
