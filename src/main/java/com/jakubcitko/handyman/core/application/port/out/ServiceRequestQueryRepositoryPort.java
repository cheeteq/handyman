package com.jakubcitko.handyman.core.application.port.out;

import com.jakubcitko.handyman.adapters.inbound.web.dto.response.AttachmentDetailsResponseDto;
import com.jakubcitko.handyman.adapters.inbound.web.dto.response.CustomerRequestDetailsResponseDto;
import com.jakubcitko.handyman.adapters.inbound.web.dto.response.CustomerRequestSummaryResponseDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ServiceRequestQueryRepositoryPort {

    List<CustomerRequestSummaryResponseDto> findSummariesByCustomerId(UUID customerId);
    Optional<CustomerRequestDetailsResponseDto> findCustomerRequestDetailsById(UUID requestId);
    List<AttachmentDetailsResponseDto> findAttachmentsByServiceRequestId(UUID requestId);
}
