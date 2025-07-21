package com.jakubcitko.handyman.core.application.port.out;

import com.jakubcitko.handyman.core.application.port.in.GenerateUploadUrlUseCase.UploadUrlResponse;

public interface FileStoragePort {
    UploadUrlResponse generatePresignedUploadUrl(String originalFilename, String contentType);
}
