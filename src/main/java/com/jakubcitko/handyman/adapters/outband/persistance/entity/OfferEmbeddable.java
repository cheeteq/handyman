package com.jakubcitko.handyman.adapters.outband.persistance.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Embeddable
public class OfferEmbeddable {

    @Column(name = "offer_estimated_cost")
    private BigDecimal estimatedCost;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "offer_available_time_slots", joinColumns = @JoinColumn(name = "service_request_id"))
    private List<TimeSlotEmbeddable> availableTimeSlots;
}
