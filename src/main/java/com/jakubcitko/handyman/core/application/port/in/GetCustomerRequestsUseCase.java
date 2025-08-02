package com.jakubcitko.handyman.core.application.port.in;

import com.jakubcitko.handyman.adapters.inbound.web.dto.CustomerRequestSummaryDto;

import java.util.List;
import java.util.UUID;

public interface GetCustomerRequestsUseCase {
    List<CustomerRequestSummaryDto> getRequestsForCustomer(GetCustomerRequestsQuery query);

    record GetCustomerRequestsQuery(
            UUID customerId
    ) {
    }
}
