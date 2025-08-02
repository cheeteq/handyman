package com.jakubcitko.handyman.adapters.outbound;

import com.jakubcitko.handyman.adapters.inbound.web.dto.CustomerRequestSummaryDto;
import com.jakubcitko.handyman.adapters.outbound.persistence.ServiceRequestQueryJpaRepository;
import com.jakubcitko.handyman.core.application.port.out.ServiceRequestQueryRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class ServiceRequestQueryPersistenceAdapter implements ServiceRequestQueryRepositoryPort {

    private final ServiceRequestQueryJpaRepository queryJpaRepository;

    public ServiceRequestQueryPersistenceAdapter(ServiceRequestQueryJpaRepository queryJpaRepository) {
        this.queryJpaRepository = queryJpaRepository;
    }

    @Override
    public List<CustomerRequestSummaryDto> findSummariesByCustomerId(UUID customerId) {
        return queryJpaRepository.findSummariesByCustomerId(customerId);
    }

}