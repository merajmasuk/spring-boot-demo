package com.example.springtutorial.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import java.net.URI;

@Configuration
@Slf4j
public class AWSConfig {

    @Value("${spring.cloud.aws.credentials.access-key}")
    private String accessKey;
    @Value("${spring.cloud.aws.credentials.secret-key}")
    private String secretKey;
    @Value("${spring.cloud.aws.s3.region}")
    private String region;
    @Value("${spring.cloud.aws.s3.endpoint}")
    private String s3endpoint;
    @Value("${spring.cloud.aws.bucket}")
    private String bucketName;

    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .region(Region.of(region))
                .endpointOverride(URI.create(s3endpoint))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKey, secretKey)
                ))
                .forcePathStyle(true)
                .build();
    }

    @Bean
    public S3Presigner s3Presigner() {
        return S3Presigner.builder()
                .region(Region.of(region))
                .endpointOverride(URI.create(s3endpoint))
                .credentialsProvider(StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(accessKey, secretKey)
                ))
                .build();
    }

    @Bean
    public S3Response createBucketIfNotExists(S3Client s3Client) {
        try {
            s3Client.headBucket(
                    HeadBucketRequest.builder().bucket(bucketName).build()
            );
            log.info("Bucket {} already exists", bucketName);
            return null;
        } catch (S3Exception e) {
            if (e.statusCode() == 404) {
                CreateBucketRequest.Builder builder = CreateBucketRequest.builder()
                        .bucket(bucketName);

                builder.createBucketConfiguration(
                        CreateBucketConfiguration.builder()
                                .locationConstraint(s3Client.serviceClientConfiguration().region().id())
                                .build()
                );

                CreateBucketRequest createBucketRequest = builder.build();
                S3Response response = s3Client.createBucket(createBucketRequest);
                log.info("Bucket {} has been created for the first time", bucketName);
                return response;
            } else {
                throw e;
            }
        }
    }

}
