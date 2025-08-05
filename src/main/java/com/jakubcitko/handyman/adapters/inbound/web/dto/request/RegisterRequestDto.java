package com.jakubcitko.handyman.adapters.inbound.web.dto.request;

public record RegisterRequestDto(
        String displayName,
        String phoneNumber,
        String email,
        String password
) {}
