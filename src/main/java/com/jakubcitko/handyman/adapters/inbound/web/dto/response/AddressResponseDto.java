package com.jakubcitko.handyman.adapters.inbound.web.dto.response;

import java.util.UUID;

public record AddressResponseDto(
        UUID id,
        String street,
        String streetNumber,
        String flatNumber,
        String city,
        String postalCode
) {}
