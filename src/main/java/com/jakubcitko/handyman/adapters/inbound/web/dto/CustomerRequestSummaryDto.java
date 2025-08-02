package com.jakubcitko.handyman.adapters.inbound.web.dto;

import java.time.Instant;
import java.util.UUID;

public record CustomerRequestSummaryDto(UUID id, String title, String status, Instant creationDate) {
}
