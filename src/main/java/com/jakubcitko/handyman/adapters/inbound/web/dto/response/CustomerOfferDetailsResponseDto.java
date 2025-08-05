package com.jakubcitko.handyman.adapters.inbound.web.dto.response;

import com.jakubcitko.handyman.adapters.inbound.web.dto.common.TimeSlotDto;

import java.math.BigDecimal;
import java.util.List;

public record CustomerOfferDetailsResponseDto(
        BigDecimal estimatedCost,
        List<TimeSlotDto> availableTimeSlots
) {
}
