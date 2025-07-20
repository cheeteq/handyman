package com.jakubcitko.handyman.adapters.inbound.web.dto;

public record LoginRequestDto(
        String email,
        String password
) {}
