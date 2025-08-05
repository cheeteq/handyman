package com.jakubcitko.handyman.adapters.outbound;

import com.jakubcitko.handyman.adapters.inbound.web.dto.response.AttachmentDetailsResponseDto;
import com.jakubcitko.handyman.adapters.inbound.web.dto.response.CustomerRequestDetailsResponseDto;
import com.jakubcitko.handyman.adapters.inbound.web.dto.response.CustomerRequestSummaryResponseDto;
import com.jakubcitko.handyman.adapters.outbound.persistence.ServiceRequestQueryJpaRepository;
import com.jakubcitko.handyman.core.application.port.out.ServiceRequestQueryRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ServiceRequestQueryPersistenceAdapter implements ServiceRequestQueryRepositoryPort {

    private final ServiceRequestQueryJpaRepository queryJpaRepository;

    public ServiceRequestQueryPersistenceAdapter(ServiceRequestQueryJpaRepository queryJpaRepository) {
        this.queryJpaRepository = queryJpaRepository;
    }

    @Override
    public List<CustomerRequestSummaryResponseDto> findSummariesByCustomerId(UUID customerId) {
        return queryJpaRepository.findSummariesByCustomerId(customerId);
    }

    @Override
    public Optional<CustomerRequestDetailsResponseDto> findCustomerRequestDetailsById(UUID requestId) {
        return queryJpaRepository.findCustomerRequestDetailsById(requestId);
    }

    @Override
    public List<AttachmentDetailsResponseDto> findAttachmentsByServiceRequestId(UUID requestId) {
        return queryJpaRepository.findAttachmentsByServiceRequestId(requestId);
    }

}