package com.jakubcitko.handyman.core.application.service;


import com.jakubcitko.handyman.core.application.port.in.InitiateFileUploadUseCase;
import com.jakubcitko.handyman.core.application.port.out.AttachmentRepositoryPort;
import com.jakubcitko.handyman.core.application.port.out.FileStoragePort;
import com.jakubcitko.handyman.core.domain.model.Attachment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class FileService implements InitiateFileUploadUseCase {

    private final AttachmentRepositoryPort attachmentRepository;
    private final FileStoragePort fileStoragePort;

    public FileService(
            AttachmentRepositoryPort attachmentRepository,
            FileStoragePort fileStoragePort
    ) {
        this.attachmentRepository = attachmentRepository;
        this.fileStoragePort = fileStoragePort;
    }
    @Override
    public List<FileUploadInfo> initiateUpload(InitiateUploadCommand command) {
        UUID uploaderId = command.uploaderId();

        return command.files().stream()
                .map(fileToUpload -> {
                    Attachment newAttachment =   Attachment.createNew(
                            fileToUpload.originalFilename(),
                            fileToUpload.contentType(),
                            fileToUpload.fileSize(),
                            uploaderId
                    );

                    attachmentRepository.save(newAttachment);

                    FileStoragePort.UploadUrlResponse urlResponse = fileStoragePort.generatePresignedUploadUrl(
                            newAttachment.id(),
                            newAttachment.contentType()
                    );

                    return new FileUploadInfo(
                            newAttachment.id(),
                            urlResponse.uploadUrl()
                    );
                })
                .toList();
    }
}