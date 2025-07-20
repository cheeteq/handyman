package com.jakubcitko.handyman.adapters.inbound.web.dto;

import java.util.List;
import java.util.UUID;

public record CreateServiceRequestDto(
        String title,
        String description,
        UUID addressId,
        List<UUID> attachmentIds
) {}
