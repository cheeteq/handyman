package com.jakubcitko.handyman.adapters.outbound.filestorage;

import com.jakubcitko.handyman.core.application.port.in.GenerateUploadUrlUseCase.UploadUrlResponse;
import com.jakubcitko.handyman.core.application.port.out.FileStoragePort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.net.URI;
import java.time.Duration;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
public class S3FileStorageAdapter implements FileStoragePort {

    private final S3Client s3Client;
    private final S3Presigner s3Presigner;
    private final String bucketName;

    public S3FileStorageAdapter(
            S3Client s3Client,
            S3Presigner s3Presigner,
            @Value("${minio.bucket-name}") String bucketName
    ) {
        this.s3Client = s3Client;
        this.s3Presigner = s3Presigner;
        this.bucketName = bucketName;
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

    @Override
    public boolean doObjectsExist(List<UUID> fileUids) {
        if (fileUids == null || fileUids.isEmpty()) {
            throw new IllegalStateException("empty list not allowed");
        }
        for (UUID fileUid : fileUids) {
            try {
                HeadObjectRequest headObjectRequest = HeadObjectRequest.builder()
                        .bucket(bucketName)
                        .key(fileUid.toString())
                        .build();
                s3Client.headObject(headObjectRequest);
            } catch (S3Exception e) {
                log.error(e.toString());
                log.error("S3 error occurred for attachmentId: {}", fileUid);
                return false;
            }
        }
        return true;
    }
}