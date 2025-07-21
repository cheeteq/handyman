package com.jakubcitko.handyman.core.application.service;


import com.jakubcitko.handyman.core.application.port.in.GenerateUploadUrlUseCase;
import com.jakubcitko.handyman.core.application.port.out.FileStoragePort;
import org.springframework.stereotype.Service;

@Service
public class FileService implements GenerateUploadUrlUseCase {
    private final FileStoragePort fileStoragePort;

    public FileService(FileStoragePort fileStoragePort) {
        this.fileStoragePort = fileStoragePort;
    }


    @Override
    public UploadUrlResponse generateUploadUrl(GenerateUploadUrlCommand command) {
        //TODO: validate fileSize

        return fileStoragePort.generatePresignedUploadUrl(
                command.originalFilename(),
                command.contentType()
        );
    }
}