package com.jakubcitko.handyman.adapters.outbound.filestorage;

import com.jakubcitko.handyman.core.application.port.in.GenerateUploadUrlUseCase.UploadUrlResponse;
import com.jakubcitko.handyman.core.application.port.out.FileStoragePort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.net.URI;
import java.time.Duration;
import java.util.Map;
import java.util.UUID;

@Component
public class S3FileStorageAdapter implements FileStoragePort {

    private final String bucketName;
    private final S3Presigner s3Presigner;

    public S3FileStorageAdapter(
            @Value("${minio.endpoint}") String endpoint,
            @Value("${minio.region:us-east-1}") String region,
            @Value("${minio.access-key}") String accessKey,
            @Value("${minio.secret-key}") String secretKey,
            @Value("${minio.bucket-name}") String bucketName
    ) {
        this.bucketName = bucketName;

        S3Configuration s3Configuration = S3Configuration.builder()
                .pathStyleAccessEnabled(true)
                .build();

        this.s3Presigner = S3Presigner.builder()
                .region(Region.of(region))
                .endpointOverride(URI.create(endpoint))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKey, secretKey)
                ))
                .serviceConfiguration(s3Configuration)
                .build();
    }

    @Override
    public UploadUrlResponse generatePresignedUploadUrl(String originalFilename, String contentType) {
        UUID fileUid = UUID.randomUUID();
        String objectKey = fileUid.toString();

        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .contentType(contentType)
                .build();

        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(10))
                .putObjectRequest(objectRequest)
                .build();

        PresignedPutObjectRequest presignedRequest = s3Presigner.presignPutObject(presignRequest);
        String signedUrl = presignedRequest.url().toString();

        return new UploadUrlResponse(signedUrl, fileUid);
    }
}