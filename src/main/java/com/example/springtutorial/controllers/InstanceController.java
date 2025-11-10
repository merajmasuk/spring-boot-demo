package com.example.springtutorial.controllers;

import com.example.springtutorial.dao.InstanceDAO;
import com.example.springtutorial.dto.BaseResponse;
import com.example.springtutorial.dto.InstanceDTO;
import com.example.springtutorial.dto.PageDTO;
import com.example.springtutorial.exceptions.BusinessException;
import com.example.springtutorial.services.InstanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/instance")
public class InstanceController {

    private final InstanceService instanceService;

    @PostMapping("/")
    public ResponseEntity<BaseResponse<?>> createInstance(@RequestBody InstanceDAO request) throws BusinessException {
        InstanceDTO response = instanceService.createInstance(request);
        return new ResponseEntity<>(
                BaseResponse.builder()
                        .status(HttpStatus.CREATED.value())
                        .data(response)
                        .build(),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<?>> getInstance(@PathVariable UUID id) throws BusinessException {
        InstanceDTO response = instanceService.getInstanceById(id);
        return ResponseEntity.ok(
                BaseResponse.builder()
                        .status(HttpStatus.OK.value())
                        .data(response)
                        .build()
        );
    }

    @GetMapping
    public ResponseEntity<BaseResponse<?>> getAllInstances(
            @RequestParam String titleLike,
            @PageableDefault(
                    sort = "lastModifiedAt",
                    direction = Sort.Direction.DESC
            ) Pageable pageable
    ) {
        PageDTO<InstanceDTO> response = instanceService.getInstancesList(titleLike, pageable);
        return ResponseEntity.ok(
                BaseResponse.builder()
                        .status(HttpStatus.OK.value())
                        .data(response)
                        .build()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<?>> updateInstance(@PathVariable UUID id, @RequestBody InstanceDAO request) throws BusinessException {
        InstanceDTO response = instanceService.updateInstance(id, request);
        return ResponseEntity.ok(
                BaseResponse.builder()
                        .status(HttpStatus.OK.value())
                        .data(response)
                        .build()
        );
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<BaseResponse<?>> deleteInstance(@PathVariable UUID id) throws BusinessException {
        instanceService.deleteInstance(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/restore/{id}")
    public ResponseEntity<BaseResponse<?>> restoreInstance(@PathVariable UUID id, @RequestBody InstanceDAO request) throws BusinessException {
        instanceService.restoreInstance(id);
        return ResponseEntity.noContent().build();
    }

}
