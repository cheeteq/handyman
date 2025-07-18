package com.jakubcitko.handyman.core.application.port.in;

import java.util.List;
import java.util.UUID;

public interface CreateServiceRequestUseCase {
    void createServiceRequest(CreateServiceRequestCommand command);

    record CreateServiceRequestCommand(
            String title,
            String description,
            UUID customerId,
            UUID addressId,
            List<UUID> attachments
    ) {
    }
}
