package com.example.springtutorial.services;

import com.example.springtutorial.utils.AWSUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CopyObjectRequest;
import software.amazon.awssdk.services.s3.model.CopyObjectResponse;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StorageService {

    @Value("${spring.cloud.aws.bucket}")
    private String bucketName;

    private static final long S3_OBJECT_EXPIRY = 1000 * 60 * 60 * 24;

    private final S3Client s3Client;
    private final AWSUtils awsUtils;

    public String uploadFile(MultipartFile file, String path) throws IOException {
        String filename = UUID.randomUUID().toString() + "." + file.getOriginalFilename();
        String key = path + "/" + filename;
        File fileObj = awsUtils.convertMultipartFileToFile(file);
        Path filePath = Paths.get(key);
        s3Client.putObject(
                PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .build(),
                filePath
        );
        fileObj.delete();
        GetObjectRequest request = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();
        s3Client.getObject(request, filePath);
        return key;
    }

    public String getObjectURL(String key) {
        return awsUtils.generatePresignedUrl(bucketName, key, S3_OBJECT_EXPIRY);
    }

    public String copyObject(String source, String destination) {
        CopyObjectRequest copyObjectRequest = CopyObjectRequest.builder()
                .sourceBucket(bucketName)
                .sourceKey(source)
                .destinationBucket(bucketName)
                .destinationKey(destination)
                .build();
        s3Client.copyObject(copyObjectRequest);
        return destination;
    }
}
