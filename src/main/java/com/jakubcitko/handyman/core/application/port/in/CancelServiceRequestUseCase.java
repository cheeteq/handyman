package com.jakubcitko.handyman.core.application.port.in;

import java.util.UUID;

public interface CancelServiceRequestUseCase {
    void cancelServiceRequest(CancelServiceRequestCommand command);

    record CancelServiceRequestCommand(
            UUID serviceRequestId
    ) {
    }
}
