package com.jakubcitko.handyman.core.domain.model;

import java.time.Instant;
import java.util.Objects;

public class TimeSlot {
    private final Instant startDate;
    private final Instant endDate;

    public TimeSlot(Instant startDate, Instant endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TimeSlot timeSlot = (TimeSlot) o;

        if (!Objects.equals(startDate, timeSlot.startDate)) return false;
        return Objects.equals(endDate, timeSlot.endDate);
    }

    @Override
    public int hashCode() {
        int result = startDate != null ? startDate.hashCode() : 0;
        result = 31 * result + (endDate != null ? endDate.hashCode() : 0);
        return result;
    }

}
