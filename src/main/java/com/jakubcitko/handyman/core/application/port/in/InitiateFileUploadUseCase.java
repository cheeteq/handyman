package com.jakubcitko.handyman.core.application.port.in;

import java.util.List;
import java.util.UUID;

public interface InitiateFileUploadUseCase {
    List<FileUploadInfo> initiateUpload(InitiateUploadCommand command);

    record FileUploadInfo(
            UUID fileId,
            String uploadUrl
    ) {}

    record InitiateUploadCommand(
            UUID uploaderId,
            List<FileToUpload> files
    ) {}

    record FileToUpload(String originalFilename, String contentType, long fileSize) {}
}