package com.example.springtutorial.services;

import com.example.springtutorial.assemblers.InstanceModelAssembler;
import com.example.springtutorial.controllers.InstanceController;
import com.example.springtutorial.dao.InstanceDAO;
import com.example.springtutorial.dto.InstanceDTO;
import com.example.springtutorial.entities.Instance;
import com.example.springtutorial.exceptions.BusinessException;
import com.example.springtutorial.mappers.InstanceMapper;
import com.example.springtutorial.repository.InstanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
@RequiredArgsConstructor
public class InstanceService {

    private final InstanceRepository instanceRepository;
    private final InstanceMapper instanceMapper;
    private final InstanceModelAssembler instanceModelAssembler;

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

    public PagedModel<EntityModel<InstanceDTO>> getInstancesList(String titleLike, Pageable pageable) {
        Page<Instance> instances = instanceRepository.findByTitleContainingAndIsDeleted(titleLike, false, pageable);
        List<InstanceDTO> dtoList = instanceMapper.toDTOList(instances.getContent());
        List<EntityModel<InstanceDTO>> entityModels = dtoList.stream()
                .map(instanceModelAssembler::toModel)
                .toList();

        PagedModel<EntityModel<InstanceDTO>> pagedModel = PagedModel.of(
                entityModels,
                new PagedModel.PageMetadata(
                        instances.getSize(),
                        instances.getNumber(),
                        instances.getTotalElements(),
                        instances.getTotalPages()
                )
        );
        pagedModel.add(
                linkTo(methodOn(InstanceController.class).getInstanceList(titleLike, pageable))
                        .withSelfRel()
        );

        if (instances.hasNext()) {
            pagedModel.add(
                    linkTo(methodOn(InstanceController.class).getInstanceList(titleLike, instances.nextPageable()))
                            .withRel("next")
            );
        }
        if (instances.hasPrevious()) {
            pagedModel.add(
                    linkTo(methodOn(InstanceController.class).getInstanceList(titleLike, instances.previousPageable()))
                            .withRel("prev")
            );
        }
        return pagedModel;
    }

    public InstanceDTO updateInstance(UUID id, InstanceDAO instanceDAO) throws BusinessException {
        return instanceRepository.findByIdAndIsDeleted(id, false)
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
        instanceRepository.findByIdAndIsDeleted(id, false)
                .map(instance -> {
                    instance.setIsDeleted(true);
                    instanceRepository.save(instance);
                    return true;
                })
                .orElseThrow(() ->
                        new BusinessException("INSTANCE_NOT_FOUND", "Instance not found or already deleted!")
                );
    }

    public void restoreInstance(UUID id) throws BusinessException {
        instanceRepository.findByIdAndIsDeleted(id, true)
                .map(instance -> {
                    instance.setIsDeleted(false);
                    instanceRepository.save(instance);
                    return true;
                })
                .orElseThrow(() ->
                        new BusinessException("INSTANCE_NOT_FOUND", "Instance not found or already restored!")
                );
    }

}
