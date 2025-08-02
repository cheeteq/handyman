package com.jakubcitko.handyman.core.application.port.out;

import com.jakubcitko.handyman.core.application.port.in.GenerateUploadUrlUseCase.UploadUrlResponse;

import java.util.List;
import java.util.UUID;

public interface FileStoragePort {
    UploadUrlResponse generatePresignedUploadUrl(String originalFilename, String contentType);

    boolean doObjectsExist(List<UUID> fileUids);
}
