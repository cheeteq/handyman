package com.jakubcitko.handyman.adapters.outbound.persistence.entity;

import com.jakubcitko.handyman.core.domain.model.AttachmentStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.UUID;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "attachments")
@Getter
@Setter
public class AttachmentEntity {

    @Id
    @Column(name = "id")
    private UUID id;
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AttachmentStatus status;

    @Column(name = "original_filename", nullable = false)
    private String originalFilename;

    @Column(name = "content_type", nullable = false)
    private String contentType;

    @Column(name = "file_size", nullable = false)
    private long fileSize;

    @Column(name = "uploader_id", nullable = false)
    private UUID uploaderId;
    @CreatedDate
    @Column(name = "creation_date", nullable = false)
    private Instant creationDate;

    @Column(name = "service_request_id")
    private UUID serviceRequestId;
}
