package com.jakubcitko.handyman.adapters.outbound.persistence.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Embeddable
@Getter
@Setter
public class TimeSlotEmbeddable {
    private Instant startDateTime;
    private Instant endDateTime;
}
