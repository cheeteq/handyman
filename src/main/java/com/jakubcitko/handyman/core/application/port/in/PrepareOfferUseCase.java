package com.jakubcitko.handyman.core.application.port.in;

import com.jakubcitko.handyman.core.domain.model.TimeSlot;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface PrepareOfferUseCase {
    void prepareOffer(PrepareOfferCommand command);

    record PrepareOfferCommand(
            UUID serviceRequestId,
            BigDecimal estimatedCost,
            List<TimeSlot> availableTimeSlots
    ) {
    }
}
