package com.jakubcitko.handyman.core.application.port.out;


import java.util.List;
import java.util.UUID;

public interface FileStoragePort {
    UploadUrlResponse generatePresignedUploadUrl(UUID fileUid, String contentType);

    boolean doObjectsExist(List<UUID> fileUids);

    record UploadUrlResponse(
            String uploadUrl,
            UUID fileUid
    ) {
    }
}
