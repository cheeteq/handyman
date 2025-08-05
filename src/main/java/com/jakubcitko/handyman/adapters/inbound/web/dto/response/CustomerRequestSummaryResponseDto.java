package com.jakubcitko.handyman.adapters.inbound.web.dto.response;

import java.time.Instant;
import java.util.UUID;

public record CustomerRequestSummaryResponseDto(UUID id, String title, String fullAddress, String status, Instant creationDate) {
}
