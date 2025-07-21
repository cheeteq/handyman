package com.jakubcitko.handyman.core.domain.model;

import java.math.BigDecimal;
import java.util.List;

public class Offer {
    private BigDecimal estimatedCost;
    private List<TimeSlot> availableTimeSlots;

    public Offer(BigDecimal estimatedCost, List<TimeSlot> availableTimeSlots) {
        this.estimatedCost = estimatedCost;
        this.availableTimeSlots = availableTimeSlots;
    }

    public BigDecimal getEstimatedCost() {
        return estimatedCost;
    }

    public List<TimeSlot> getAvailableTimeSlots() {
        return availableTimeSlots;
    }
}
