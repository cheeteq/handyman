package com.jakubcitko.handyman.adapters.outband.persistance.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

/**
 * Encja JPA reprezentująca adres.
 * Jest to encja podrzędna w agregacie Customer.
 */
@Entity
@Table(name = "addresses")
@Getter
@Setter
public class AddressEntity {
    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "street", nullable = false)
    private String street;

    @Column(name = "street_number", nullable = false)
    private String streetNumber;

    @Column(name = "flat_number")
    private String flatNumber;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "postal_code", nullable = false)
    private String postalCode;
}
