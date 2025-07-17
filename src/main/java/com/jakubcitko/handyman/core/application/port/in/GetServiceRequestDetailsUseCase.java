package com.jakubcitko.handyman.core.application.port.in;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface GetServiceRequestDetailsUseCase {
    ServiceRequestDetails query(GetServiceRequestDetailsQuery query);

    record GetServiceRequestDetailsQuery(
            UUID serviceRequestId,
            UUID requesterId
    ) {
    }

    record ServiceRequestDetails(
            UUID id,
            String title,
            String description,
            String status,
            AddressDetails address,
            OfferDetails offer,
            List<FileReferenceDetails> attachments,
            String handymanNote,
            RevenueDetails revenue
    ) {
    }

    record AddressDetails(String street, String city, String postalCode) {
    }

    record OfferDetails(BigDecimal estimatedCost, List<TimeSlotDetails> availableTimeSlots,
                        TimeSlotDetails chosenTimeSlot) {
    }

    record TimeSlotDetails(LocalDateTime start, LocalDateTime end) {
    }

    record FileReferenceDetails(UUID fileId, String originalFilename) {
    }

    record RevenueDetails(BigDecimal revenue, BigDecimal costs, BigDecimal profit) {
    }
}
