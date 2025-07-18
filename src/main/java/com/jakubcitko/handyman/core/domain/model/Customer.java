package com.jakubcitko.handyman.core.domain.model;

import com.jakubcitko.handyman.core.domain.exception.AddressAlreadyExistsException;

import java.util.List;
import java.util.UUID;

public class Customer {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private List<Address> addresses;

    public void addAddress(Address newAddress) {
        if (this.addresses.contains(newAddress)) {
            throw new AddressAlreadyExistsException("This address is already associated with the customer.");
        }
        this.addresses.add(newAddress);
    }

    private Customer(UUID id, String firstName, String lastName, String email, String phoneNumber) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public static Customer registerNew(String firstName, String lastName, String phone, String email) {
        return new Customer(UUID.randomUUID(), firstName, lastName, phone, email);
    }

    public UUID getId() {
        return id;
    }
}
