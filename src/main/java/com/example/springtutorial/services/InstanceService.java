package com.example.springtutorial.services;

import com.example.springtutorial.dao.InstanceDAO;
import com.example.springtutorial.dto.InstanceDTO;
import com.example.springtutorial.dto.PageDTO;
import com.example.springtutorial.entities.Instance;
import com.example.springtutorial.exceptions.BusinessException;
import com.example.springtutorial.mappers.InstanceMapper;
import com.example.springtutorial.repository.InstanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
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
            throw new BusinessException(
                    "INSTANCE_ALREADY_EXISTS",
                    "The instance you trying to create already exists!"
            );
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

    public InstanceDTO getInstanceById(UUID id) throws BusinessException {
        return instanceRepository.findById(id)
                .map(instanceMapper::toDTO)
                .orElseThrow(() ->
                        new BusinessException("INSTANCE_NOT_FOUND", "Instance not found!")
                );
    }

    public PageDTO<InstanceDTO> getInstancesList(String title, Pageable pageable) {
        Page<Instance> instances = instanceRepository.findByTitleContaining(title, pageable);
        List<InstanceDTO> responseDTOs = instanceMapper.toDTOList(instances.getContent());
        return PageDTO.<InstanceDTO>builder()
                .currentPage(instances.getNumber())
                .totalItems(instances.getTotalElements())
                .totalPages(instances.getTotalPages())
                .data(responseDTOs)
                .build();
    }

    public InstanceDTO updateInstance(UUID id, InstanceDAO instanceDAO) throws BusinessException {
        return instanceRepository.findById(id)
                .map(instance ->  {
                    instance.setTitle(instanceDAO.getTitle());
                    instance.setDescription(instanceDAO.getDescription());
                    instanceRepository.save(instance);
                    return instanceMapper.toDTO(instance);
                })
                .orElseThrow(() ->
                        new BusinessException("INSTANCE_NOT_FOUND", "Instance not found!")
                );
    }

    public void deleteInstance(UUID id) throws BusinessException {
        instanceRepository.findById(id)
                .map(instance -> {
                    instance.setIsDeleted(true);
                    instanceRepository.save(instance);
                    return true;
                })
                .orElseThrow(() ->
                        new BusinessException("INSTANCE_NOT_FOUND", "Instance not found!")
                );
    }

    public void restoreInstance(UUID id) throws BusinessException {
        instanceRepository.findById(id)
                .map(instance -> {
                    instance.setIsDeleted(false);
                    instanceRepository.save(instance);
                    return true;
                })
                .orElseThrow(() ->
                        new BusinessException("INSTANCE_NOT_FOUND", "Instance not found!")
                );
    }

}
