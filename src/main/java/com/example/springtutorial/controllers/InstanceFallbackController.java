package com.example.springtutorial.controllers;

import com.example.springtutorial.dto.BaseResponse;
import com.example.springtutorial.entities.Instance;
import com.example.springtutorial.repository.InstanceAccessRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@RepositoryRestController
@RequiredArgsConstructor
public class InstanceFallbackController {

    private final InstanceAccessRepository instanceAccessRepository;

    @GetMapping("/search/byTitle")
    public ResponseEntity<BaseResponse<?>> searchByName(String title){
        List<Instance> response = instanceAccessRepository.findByTitleContainingAndIsDeleted(title, false);
        return ResponseEntity.ok(
                BaseResponse.builder()
                        .success(true)
                        .data(response)
                        .build()
        );
    }
}
