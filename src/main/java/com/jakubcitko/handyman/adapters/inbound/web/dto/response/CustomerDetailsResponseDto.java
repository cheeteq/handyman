package com.jakubcitko.handyman.adapters.inbound.web.dto.response;

import java.util.UUID;

public record CustomerDetailsResponseDto(
        UUID id,
        String displayName,
        String phoneNumber
) {}
