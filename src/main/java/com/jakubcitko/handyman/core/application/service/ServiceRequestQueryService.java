package com.jakubcitko.handyman.core.application.service;

import com.jakubcitko.handyman.adapters.inbound.web.dto.CustomerRequestSummaryDto;
import com.jakubcitko.handyman.core.application.port.in.GetCustomerRequestsUseCase;
import com.jakubcitko.handyman.core.application.port.out.ServiceRequestQueryRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ServiceRequestQueryService implements GetCustomerRequestsUseCase {
    private final ServiceRequestQueryRepositoryPort queryRepository;

    public ServiceRequestQueryService(ServiceRequestQueryRepositoryPort queryRepository) {
        this.queryRepository = queryRepository;
    }

    @Override
    public List<CustomerRequestSummaryDto> getRequestsForCustomer(GetCustomerRequestsQuery query) {
        return queryRepository.findSummariesByCustomerId(query.customerId());
    }
}
