package com.jakubcitko.handyman.core.domain.model;

import com.jakubcitko.handyman.core.domain.exception.AddressAlreadyExistsException;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class Customer {
    private final UUID id;
    private final String displayName;
    private String phoneNumber;
    private List<Address> addresses;

    public void addAddress(Address newAddress) {
        if (this.addresses.contains(newAddress)) {
            throw new AddressAlreadyExistsException("This address is already associated with the customer.");
        }
        this.addresses.add(newAddress);
    }

    public Customer(UUID id, String displayName, String phoneNumber) {
        this.id = id;
        this.displayName = displayName;
        this.phoneNumber = phoneNumber;
        this.addresses = new ArrayList<>();
    }

    public static Customer registerNew(UUID userId, String displayName, String phone) {
        return new Customer(userId, displayName, phone);
    }

}
