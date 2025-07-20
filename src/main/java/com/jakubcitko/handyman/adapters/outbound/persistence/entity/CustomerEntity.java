package com.jakubcitko.handyman.adapters.outbound.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Encja JPA reprezentująca klienta w bazie danych.
 * Jest to korzeń agregatu, który zawiera w sobie listę adresów.
 */
@Entity
@Table(name = "customers")
@Getter
@Setter
public class CustomerEntity {

    @Id
    @Column(name = "user_id")
    private UUID id;

    @Column(name = "display_name", nullable = false)
    private String displayName;

    @Column(name = "phone_number")
    private String phoneNumber;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @JoinColumn(name = "customer_id", nullable = false)
    private List<AddressEntity> addresses = new ArrayList<>();

    @Version
    private Long version;
}
