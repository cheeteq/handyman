package com.jakubcitko.handyman.adapters.inbound.web.dto.request;

public record AddAddressRequestDto(
        String street,
        String streetNumber,
        String flatNumber,
        String city,
        String postalCode
) {}