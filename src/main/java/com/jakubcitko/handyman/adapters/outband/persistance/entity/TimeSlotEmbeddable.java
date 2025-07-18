package com.jakubcitko.handyman.adapters.outband.persistance.entity;

import jakarta.persistence.Embeddable;
import java.time.LocalDateTime;

@Embeddable
public class TimeSlotEmbeddable {
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
}
