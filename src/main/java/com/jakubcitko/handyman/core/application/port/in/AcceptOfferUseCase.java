package com.jakubcitko.handyman.core.application.port.in;

import java.util.UUID;

public interface AcceptOfferUseCase {
    void acceptOffer(AcceptOfferCommand command);
    record AcceptOfferCommand(
            UUID serviceRequestId
    ) {}
}
