package com.jakubcitko.handyman.core.domain.model;

import java.time.LocalDateTime;

public class TimeSlot {
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public TimeSlot(LocalDateTime startDate, LocalDateTime endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
