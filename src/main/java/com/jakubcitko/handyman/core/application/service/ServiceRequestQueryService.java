package com.jakubcitko.handyman.core.application.service;

import com.jakubcitko.handyman.adapters.inbound.web.dto.response.AttachmentDetailsResponseDto;
import com.jakubcitko.handyman.adapters.inbound.web.dto.response.CustomerRequestDetailsResponseDto;
import com.jakubcitko.handyman.adapters.inbound.web.dto.response.CustomerRequestSummaryResponseDto;
import com.jakubcitko.handyman.core.application.port.in.GetCustomerRequestDetailsUseCase;
import com.jakubcitko.handyman.core.application.port.in.GetCustomerRequestsSummariesUseCase;
import com.jakubcitko.handyman.core.application.port.out.ServiceRequestQueryRepositoryPort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class ServiceRequestQueryService implements GetCustomerRequestsSummariesUseCase, GetCustomerRequestDetailsUseCase {
    private final ServiceRequestQueryRepositoryPort queryRepository;

    public ServiceRequestQueryService(ServiceRequestQueryRepositoryPort queryRepository) {
        this.queryRepository = queryRepository;
    }

    @Override
    public List<CustomerRequestSummaryResponseDto> getRequestsForCustomer(GetCustomerRequestsQuery query) {
        return queryRepository.findSummariesByCustomerId(query.customerId());
    }

    @Override
    public Optional<CustomerRequestDetailsResponseDto> getDetails(GetCustomerRequestDetailsQuery query) {
        Optional<CustomerRequestDetailsResponseDto> baseDetailsOpt =
                queryRepository.findCustomerRequestDetailsById(query.serviceRequestId());

        if (baseDetailsOpt.isEmpty()) {
            return Optional.empty();
        }

        CustomerRequestDetailsResponseDto baseDetails = baseDetailsOpt.get();

        authorizeCustomerAccess(query.requesterId(), baseDetails);

        List<AttachmentDetailsResponseDto> attachments =
                queryRepository.findAttachmentsByServiceRequestId(query.serviceRequestId());

        CustomerRequestDetailsResponseDto finalDetails = baseDetails
                .withAttachments(attachments);
                //TODO: withOffer

        return Optional.of(finalDetails);
    }

    private void authorizeCustomerAccess(UUID requesterId, CustomerRequestDetailsResponseDto details) {
        if (!details.customer().id().equals(requesterId)) {
            throw new AccessDeniedException("Access Denied: You do not own this resource.");
        }
    }
}
