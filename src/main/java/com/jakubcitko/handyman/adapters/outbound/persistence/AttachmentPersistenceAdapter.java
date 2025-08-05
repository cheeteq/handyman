package com.jakubcitko.handyman.adapters.outbound.persistence;

import com.jakubcitko.handyman.adapters.outbound.persistence.entity.AttachmentEntity;
import com.jakubcitko.handyman.adapters.outbound.persistence.mapper.AttachmentPersistenceMapper;
import com.jakubcitko.handyman.core.application.port.out.AttachmentRepositoryPort;
import com.jakubcitko.handyman.core.domain.model.Attachment;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@Profile({"test", "dev"})
public class AttachmentPersistenceAdapter implements AttachmentRepositoryPort {

    private final AttachmentJpaRepository jpaRepository;
    private final AttachmentPersistenceMapper mapper;

    public AttachmentPersistenceAdapter(AttachmentJpaRepository jpaRepository, AttachmentPersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public void save(Attachment attachment) {
        AttachmentEntity entity = mapper.toEntity(attachment);
        jpaRepository.save(entity);
    }

    @Override
    public List<Attachment> findAllByIds(List<UUID> attachmentIds) {
        List<AttachmentEntity> foundEntities = jpaRepository.findAllById(attachmentIds);
        return mapper.toDomainList(foundEntities);
    }
}
