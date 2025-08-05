package com.jakubcitko.handyman.core.application.port.out;

import com.jakubcitko.handyman.core.domain.model.Attachment;

import java.util.List;
import java.util.UUID;

public interface AttachmentRepositoryPort {

    void save(Attachment attachment);

    List<Attachment> findAllByIds(List<UUID> attachmentIds);
}
