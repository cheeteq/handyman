package com.jakubcitko.handyman.core.application.port.out;

import com.jakubcitko.handyman.core.domain.model.ServiceRequest;

import java.util.Optional;
import java.util.UUID;

public interface ServiceRequestRepositoryPort {
    UUID save(ServiceRequest serviceRequest);

    Optional<ServiceRequest> findById(UUID id);
}
