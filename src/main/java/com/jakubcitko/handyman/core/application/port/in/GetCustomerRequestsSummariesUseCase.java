package com.jakubcitko.handyman.core.application.port.in;

import com.jakubcitko.handyman.adapters.inbound.web.dto.response.CustomerRequestSummaryResponseDto;

import java.util.List;
import java.util.UUID;

public interface GetCustomerRequestsSummariesUseCase {
    List<CustomerRequestSummaryResponseDto> getRequestsForCustomer(GetCustomerRequestsQuery query);

    record GetCustomerRequestsQuery(
            UUID customerId
    ) {
    }
}
