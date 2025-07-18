package com.jakubcitko.handyman.core.application.port.out;

import com.jakubcitko.handyman.core.domain.model.Customer;

import java.util.Optional;
import java.util.UUID;

public interface CustomerRepositoryPort {
    void save(Customer customer);
    Optional<Customer> findById(UUID id);
}