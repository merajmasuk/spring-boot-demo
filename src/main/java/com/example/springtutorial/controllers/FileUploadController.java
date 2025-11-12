package com.example.springtutorial.controllers;

import com.example.springtutorial.dto.BaseResponse;
import com.example.springtutorial.services.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/file")
public class FileUploadController {

    private final StorageService storageService;

    @PostMapping("/upload")
    public ResponseEntity<BaseResponse<?>> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "path", defaultValue = "uploads") String path
    ) throws IOException {
        String key = storageService.uploadFile(file, path);
        String url = storageService.getObjectURL(key);

        Map<String, String> response = Map.of(
                "key", key,
                "url", url
        );
        return ResponseEntity.ok(
                BaseResponse.builder()
                        .success(true)
                        .data(response)
                        .build()
        );
    }

    @GetMapping("/list")
    public ResponseEntity<BaseResponse<?>> listUploadedFiles(
            @RequestParam(value = "path", required = false) String path
    ) {
        List<Map<String, Object>> files = storageService.listObjects(path);
        return ResponseEntity.ok(
                BaseResponse.builder()
                        .success(true)
                        .data(files)
                        .build()
        );
    }
}
