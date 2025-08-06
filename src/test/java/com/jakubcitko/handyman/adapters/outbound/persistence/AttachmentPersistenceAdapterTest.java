package com.jakubcitko.handyman.adapters.outbound.persistence;

import com.jakubcitko.handyman.AbstractSpringBootTest;
import com.jakubcitko.handyman.core.domain.model.Attachment;
import com.jakubcitko.handyman.core.domain.model.AttachmentStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AttachmentPersistenceAdapterTest extends AbstractSpringBootTest {
    @Autowired
    AttachmentPersistenceAdapter persistenceAdapter;

    @Test
    void test_attachmentSaveAndFindAllByIds() {
        //GIVEN
        String originalFilename = "originalName.jpg";
        String contentType = "image/jpeg";
        long fileSize = 204800;
        UUID uploaderId = UUID.randomUUID();

        Attachment attachment = Attachment.createNew(originalFilename, contentType, fileSize, uploaderId);
        UUID attachmentId = attachment.id();

        //WHEN
        persistenceAdapter.save(attachment);

        //THEN
        var savedAttachmentList = persistenceAdapter.findAllByIds(List.of(attachmentId));
        assertEquals(1, savedAttachmentList.size());

        var savedAttachment = savedAttachmentList.getFirst();
        assertEquals(attachmentId, savedAttachment.id());
        assertEquals(AttachmentStatus.PENDING, savedAttachment.status());
        assertEquals(originalFilename, savedAttachment.originalFilename());
        assertEquals(contentType, savedAttachment.contentType());
        assertEquals(fileSize, savedAttachment.fileSize());
        assertEquals(uploaderId, savedAttachment.uploaderId());
        assertNull(savedAttachment.serviceRequestId());
        assertNotNull(savedAttachment.creationDate());
    }

}