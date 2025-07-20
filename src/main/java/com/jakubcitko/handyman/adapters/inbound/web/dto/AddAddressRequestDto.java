package com.jakubcitko.handyman.adapters.inbound.web.dto;

public record AddAddressRequestDto(
        String street,
        String streetNumber,
        String flatNumber,
        String city,
        String postalCode
) {}