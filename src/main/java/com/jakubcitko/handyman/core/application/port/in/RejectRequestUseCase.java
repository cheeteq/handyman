package com.jakubcitko.handyman.core.application.port.in;

import java.util.UUID;

public interface RejectRequestUseCase {
    void rejectRequest(RejectRequestCommand command);

    record RejectRequestCommand(
            UUID serviceRequestId,
            String note
    ) {
    }
}
