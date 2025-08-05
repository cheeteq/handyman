package com.jakubcitko.handyman.adapters.outbound.persistence.mapper;

import com.jakubcitko.handyman.adapters.outbound.persistence.entity.AttachmentEntity;
import com.jakubcitko.handyman.core.domain.model.Attachment;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AttachmentPersistenceMapper {

    AttachmentEntity toEntity(Attachment domainObject);

    Attachment toDomain(AttachmentEntity entity);

    List<Attachment> toDomainList(List<AttachmentEntity> entities);
}
