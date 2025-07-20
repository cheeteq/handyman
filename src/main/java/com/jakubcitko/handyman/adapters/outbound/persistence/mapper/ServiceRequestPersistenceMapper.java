package com.jakubcitko.handyman.adapters.outbound.persistence.mapper;

import com.jakubcitko.handyman.adapters.outbound.persistence.entity.ServiceRequestEntity;
import com.jakubcitko.handyman.core.domain.model.ServiceRequest;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring", uses = { CommonPersistenceMapper.class } )
public interface ServiceRequestPersistenceMapper {
    ServiceRequestEntity toEntity(ServiceRequest domainObject);

    ServiceRequest toDomain(ServiceRequestEntity entity);
}