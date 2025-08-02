package com.jakubcitko.handyman.adapters.outbound.persistence;

import com.jakubcitko.handyman.adapters.outbound.persistence.mapper.ServiceRequestPersistenceMapper;
import com.jakubcitko.handyman.core.application.port.out.ServiceRequestRepositoryPort;
import com.jakubcitko.handyman.core.domain.model.ServiceRequest;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


@Repository
@Profile({"test", "dev"})
public class ServiceRequestPersistenceAdapter implements ServiceRequestRepositoryPort {

    private final ServiceRequestJpaRepository jpaRepository;
    private final ServiceRequestPersistenceMapper mapper;

    public ServiceRequestPersistenceAdapter(
            ServiceRequestJpaRepository jpaRepository,
            ServiceRequestPersistenceMapper mapper
    ) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public UUID save(ServiceRequest serviceRequest) {
        var entity = mapper.toEntity(serviceRequest);
        return jpaRepository.save(entity).getId();
    }

    @Override
    public Optional<ServiceRequest> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }
}