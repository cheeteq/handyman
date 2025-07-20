package com.jakubcitko.handyman.core.application.port.out;

import com.jakubcitko.handyman.core.domain.model.Customer;
import com.jakubcitko.handyman.core.domain.model.Address;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerRepositoryPort {
    void save(Customer customer);
    void update(Customer customer);
    Optional<Customer> findById(UUID id);
    List<Address> findAddressesByCustomerId(UUID customerId);
}