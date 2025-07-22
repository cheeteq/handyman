package com.jakubcitko.handyman.core.domain.model;

import lombok.Getter;

import java.util.Objects;
import java.util.UUID;

@Getter
public class Address {

    private final UUID id;
    private final String street;
    private final String streetNumber;
    private final String flatNumber;
    private final String city;
    private final String postalCode;

    public Address(UUID id, String street, String streetNumber, String flatNumber, String city, String postalCode) {
        Objects.requireNonNull(id, "Address ID cannot be null");
        Objects.requireNonNull(street, "Street cannot be null");
        Objects.requireNonNull(city, "City cannot be null");
        Objects.requireNonNull(postalCode, "Postal code cannot be null");

        this.id = id;
        this.street = street;
        this.streetNumber = streetNumber;
        this.flatNumber = flatNumber;
        this.city = city;
        this.postalCode = postalCode;
    }

    public static Address createNew(String street, String streetNumber, String flatNumber, String city, String postalCode) {
        return new Address(
                UUID.randomUUID(),
                street,
                streetNumber,
                flatNumber,
                city,
                postalCode
        );
    }

}
