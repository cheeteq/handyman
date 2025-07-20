package com.jakubcitko.handyman.adapters.outbound.persistence;

import com.jakubcitko.handyman.core.application.port.out.ServiceRequestRepositoryPort;
import com.jakubcitko.handyman.core.domain.model.ServiceRequest;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
@Profile({"mock"})
public class ServiceRequestMockRepository implements ServiceRequestRepositoryPort {
    private final Map<UUID, ServiceRequest> serviceRequests = new HashMap();

    @Override
    public void save(ServiceRequest serviceRequest) {
        serviceRequests.put(serviceRequest.getId(), serviceRequest);
    }

    @Override
    public Optional<ServiceRequest> findById(UUID id) {
        return Optional.ofNullable(serviceRequests.get(id));
    }
}
