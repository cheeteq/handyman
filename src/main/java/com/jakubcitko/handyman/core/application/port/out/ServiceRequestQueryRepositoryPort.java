package com.jakubcitko.handyman.core.application.port.out;

import com.jakubcitko.handyman.adapters.inbound.web.dto.CustomerRequestSummaryDto;

import java.util.List;
import java.util.UUID;

public interface ServiceRequestQueryRepositoryPort {

    List<CustomerRequestSummaryDto> findSummariesByCustomerId(UUID customerId);
}
