package com.jakubcitko.handyman.adapters.outbound.persistence;

import com.jakubcitko.handyman.core.application.port.out.CustomerRepositoryPort;
import com.jakubcitko.handyman.core.domain.model.Customer;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@Profile({"mock"})
public class CustomerMockRepository implements CustomerRepositoryPort {
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
