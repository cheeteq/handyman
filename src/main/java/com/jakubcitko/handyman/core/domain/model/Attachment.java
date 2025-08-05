package com.jakubcitko.handyman.core.domain.model;

import java.time.Instant;
import java.util.UUID;

public record Attachment(
        UUID id,
        AttachmentStatus status,
        String originalFilename,
        String contentType,
        long fileSize,
        UUID uploaderId,
        Instant creationDate,
        UUID serviceRequestId
) {
    public static Attachment createNew(String originalFilename, String contentType, long fileSize, UUID uploaderId) {
        return new Attachment(
                UUID.randomUUID(),
                AttachmentStatus.PENDING,
                originalFilename,
                contentType,
                fileSize,
                uploaderId,
                null,
                null
        );
    }

    public Attachment assignToServiceRequest(UUID serviceRequestId) {
        return new Attachment(
                this.id,
                AttachmentStatus.ATTACHED,
                this.originalFilename,
                this.contentType,
                this.fileSize,
                this.uploaderId,
                this.creationDate,
                serviceRequestId
        );
    }
}
