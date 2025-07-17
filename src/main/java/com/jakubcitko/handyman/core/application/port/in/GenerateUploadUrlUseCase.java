package com.jakubcitko.handyman.core.application.port.in;

import java.util.UUID;

public interface GenerateUploadUrlUseCase {
    UploadUrlResponse generateUploadUrl(GenerateUploadUrlCommand command);

    record GenerateUploadUrlCommand(
            String originalFilename,
            String contentType,
            long fileSize
    ) {
    }

    record UploadUrlResponse(
            String uploadUrl,
            UUID fileUid
    ) {
    }
}