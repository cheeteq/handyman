package com.jakubcitko.handyman.adapters.inbound.web.dto.response;

import java.util.List;
import java.util.UUID;

public record JwtResponseDto(
        String accessToken,
        String tokenType,
        UUID id,
        String email,
        List<String> roles
) {
    public JwtResponseDto(String accessToken, UUID id, String email, List<String> roles) {
        this(accessToken, "Bearer", id, email, roles);
    }
}
