package com.jakubcitko.handyman.adapters.inbound.web.dto.response;

import com.jakubcitko.handyman.adapters.inbound.web.dto.common.TimeSlotDto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record CustomerRequestDetailsResponseDto(
        UUID id,
        String title,
        String description,
        String status,
        CustomerDetailsResponseDto customer,
        String fullAddress,
        List<AttachmentDetailsResponseDto> attachments,
        CustomerOfferDetailsResponseDto offer,
        TimeSlotDto chosenTimeSlot
) {
    public CustomerRequestDetailsResponseDto(
            UUID id,
            String title,
            String description,
            String status,
            UUID customerId, String customerDisplayName, String customerPhoneNumber,
            String fullAddress,
            BigDecimal offerEstimatedCost,
            Instant chosenSlotStart,
            Instant chosenSlotEnd
    ) {
        this(
                id,
                title,
                description,
                status,
                new CustomerDetailsResponseDto(customerId, customerDisplayName, customerPhoneNumber),
                fullAddress,
                null,
                offerEstimatedCost != null
                        ? new CustomerOfferDetailsResponseDto(offerEstimatedCost, null)
                        : null,
                (chosenSlotStart != null && chosenSlotEnd != null)
                        ? new TimeSlotDto(chosenSlotStart, chosenSlotEnd)
                        : null
        );
    }

    public CustomerRequestDetailsResponseDto withAttachments(List<AttachmentDetailsResponseDto> attachments) {
        return new CustomerRequestDetailsResponseDto(
                this.id,
                this.title,
                this.description,
                this.status,
                this.customer,
                this.fullAddress,
                attachments,
                this.offer,
                this.chosenTimeSlot
        );
    }
}
