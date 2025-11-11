package com.example.springtutorial.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AWSUtils {

    private final S3Presigner presigner;

    public File convertMultipartFileToFile(MultipartFile file) throws IOException {
        File fileObj = new File(Objects.requireNonNull(file.getOriginalFilename()));
        try (FileOutputStream fos = new FileOutputStream(fileObj)) {
            fos.write(file.getBytes());
        }
        return fileObj;
    }

    public String generatePresignedUrl(String bucketName, String key, long duration) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();
        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMillis(duration))
                .getObjectRequest(getObjectRequest)
                .build();
        return presigner.presignGetObject(presignRequest).url().toString();
    }
}
