package com.example.springtutorial.services;

import com.example.springtutorial.utils.AWSUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StorageService {

    @Value("${spring.cloud.aws.bucket}")
    private String bucketName;

    private static final long S3_OBJECT_EXPIRY = 1000 * 60 * 60 * 24;

    private final S3Client s3Client;
    private final AWSUtils awsUtils;

    public String uploadFile(MultipartFile file, String path) throws IOException {
        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        String key = path + "/" + filename;
        s3Client.putObject(
                PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .build(),
                RequestBody.fromInputStream(file.getInputStream(), file.getSize())
        );
        return key;
    }

    public byte[] downloadFile(String key) {
        GetObjectRequest request = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();
        ResponseBytes<GetObjectResponse> objectBytes = s3Client.getObjectAsBytes(request);
        return objectBytes.asByteArray();
    }

    public void deleteFile(String key) {
        s3Client.deleteObject(
                DeleteObjectRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .build()
        );
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

    public List<Map<String, Object>> listObjects(String prefix) {
        ListObjectsV2Request request =  ListObjectsV2Request.builder()
                .bucket(bucketName)
                .prefix(prefix != null ? prefix: "")
                .build();
        ListObjectsV2Response response = s3Client.listObjectsV2(request);
        return response.contents().stream()
                .map(obj -> {
                    Map<String, Object> fileinfo = new HashMap<>();
                    fileinfo.put("key", obj.key());
                    fileinfo.put("size", obj.size());
                    fileinfo.put("lastModified", obj.lastModified().toString());
                    fileinfo.put("url", getObjectURL(obj.key()));
                    return fileinfo;
                })
                .collect(Collectors.toList());
    }
}
