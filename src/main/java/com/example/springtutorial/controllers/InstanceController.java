package com.example.springtutorial.controllers;

import com.example.springtutorial.assemblers.InstanceModelAssembler;
import com.example.springtutorial.dao.InstanceDAO;
import com.example.springtutorial.dto.BaseResponse;
import com.example.springtutorial.dto.InstanceDTO;
import com.example.springtutorial.exceptions.BusinessException;
import com.example.springtutorial.services.InstanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/instance")
public class InstanceController {

    private final InstanceService instanceService;
    private final InstanceModelAssembler instanceModelAssembler;

    @PostMapping("/")
    public ResponseEntity<BaseResponse<?>> createInstance(@RequestBody InstanceDAO request) throws BusinessException {
        InstanceDTO response = instanceService.createInstance(request);
        EntityModel<InstanceDTO> entityModel = EntityModel.of(response);
        return new ResponseEntity<>(
                BaseResponse.builder()
                        .success(true)
                        .data(entityModel)
                        .build(),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<?>> getInstance(@PathVariable UUID id) throws BusinessException {
        InstanceDTO response = instanceService.getInstanceById(id);
        EntityModel<InstanceDTO> entityModel = instanceModelAssembler.toModel(response);
        return ResponseEntity.ok(
                BaseResponse.builder()
                        .success(true)
                        .data(entityModel)
                        .build()
        );
    }

    @GetMapping
    public ResponseEntity<BaseResponse<?>> getInstanceList(
            @RequestParam(required = false, defaultValue = "") String titleLike,
            @PageableDefault(
                    sort = "lastModifiedAt",
                    direction = Sort.Direction.DESC
            ) Pageable pageable
    ) {
        PagedModel<EntityModel<InstanceDTO>> response = instanceService.getInstancesList(titleLike, pageable);
        return ResponseEntity.ok(
                BaseResponse.builder()
                        .success(true)
                        .data(response)
                        .build()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<?>> updateInstance(@PathVariable UUID id, @RequestBody InstanceDAO request) throws BusinessException {
        InstanceDTO response = instanceService.updateInstance(id, request);
        EntityModel<InstanceDTO> entityModel = instanceModelAssembler.toModel(response);
        return ResponseEntity.ok(
                BaseResponse.builder()
                        .success(true)
                        .data(entityModel)
                        .build()
        );
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<BaseResponse<?>> deleteInstance(@PathVariable UUID id) throws BusinessException {
        instanceService.deleteInstance(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/restore/{id}")
    public ResponseEntity<BaseResponse<?>> restoreInstance(@PathVariable UUID id, @RequestBody InstanceDAO request) throws BusinessException {
        instanceService.restoreInstance(id);
        return ResponseEntity.noContent().build();
    }

}
