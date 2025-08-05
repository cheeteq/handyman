package com.jakubcitko.handyman.adapters.inbound.web.dto.request;

public record LoginRequestDto(
        String email,
        String password
) {}
