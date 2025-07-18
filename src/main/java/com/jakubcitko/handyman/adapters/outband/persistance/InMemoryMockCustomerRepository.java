package com.jakubcitko.handyman.adapters.outband.persistance;

import com.jakubcitko.handyman.core.application.port.out.CustomerRepositoryPort;
import com.jakubcitko.handyman.core.domain.model.Customer;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class InMemoryMockCustomerRepository implements CustomerRepositoryPort {
    private final Map<UUID, Customer> customers = new HashMap();

    @Override
    public void save(Customer customer) {
        customers.put(customer.getId(), customer);
    }

    @Override
    public Optional<Customer> findById(UUID id) {
        return Optional.ofNullable(customers.get(id));
    }
}
