package com.jakubcitko.handyman.core.application.port.in;

import java.util.UUID;

public interface RejectOfferUseCase {
    void rejectOffer(RejectOfferCommand command);
    record RejectOfferCommand(
            UUID serviceRequestId
    ) {}
}
