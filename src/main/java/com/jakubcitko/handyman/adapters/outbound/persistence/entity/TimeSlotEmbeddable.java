package com.jakubcitko.handyman.adapters.outbound.persistence.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Embeddable
@Getter
@Setter
public class TimeSlotEmbeddable {
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
}
