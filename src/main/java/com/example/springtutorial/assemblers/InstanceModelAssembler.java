package com.example.springtutorial.assemblers;

import com.example.springtutorial.controllers.InstanceController;
import com.example.springtutorial.dto.InstanceDTO;
import com.example.springtutorial.exceptions.BusinessException;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class InstanceModelAssembler implements RepresentationModelAssembler<InstanceDTO, EntityModel<InstanceDTO>> {

    @Override
    @NonNull
    public EntityModel<InstanceDTO> toModel(@NonNull InstanceDTO instance) {
        try {
            return EntityModel.of(instance,
                    linkTo(methodOn(InstanceController.class).getInstance(instance.getId())).withSelfRel(),
                    linkTo(methodOn(InstanceController.class).getInstanceList(instance.getTitle(), Pageable.unpaged())).withRel("instances")
            );
        } catch (BusinessException e) {
            throw new RuntimeException(e);
        }
    }

}
