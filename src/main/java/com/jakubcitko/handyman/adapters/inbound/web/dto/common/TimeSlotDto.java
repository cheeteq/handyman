package com.jakubcitko.handyman.adapters.inbound.web.dto.common;

import java.time.Instant;

public record TimeSlotDto (
        Instant start,
        Instant end
) {}