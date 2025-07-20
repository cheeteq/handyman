package com.jakubcitko.handyman.core.application.service;

import com.jakubcitko.handyman.core.application.port.in.AddAddressToCustomerUseCase;
import com.jakubcitko.handyman.core.application.port.in.RegisterCustomerUseCase;
import com.jakubcitko.handyman.core.application.port.out.CustomerRepositoryPort;
import com.jakubcitko.handyman.core.domain.model.Address;
import com.jakubcitko.handyman.core.domain.model.Customer;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CustomerService implements RegisterCustomerUseCase, AddAddressToCustomerUseCase {

    private final CustomerRepositoryPort customerRepository;

    public CustomerService(CustomerRepositoryPort customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public void registerCustomer(RegisterCustomerCommand command) {
        Customer newCustomer = Customer.registerNew(
                command.firstName(),
                command.lastName(),
                command.email(),
                command.phoneNumber()
        );

        customerRepository.save(newCustomer);
    }

    @Override
    public void addAddressToCustomer(AddAddressToCustomerCommand command) {
        Customer customer = customerRepository.findById(command.customerId())
                .orElseThrow(() -> new CustomerNotFoundException("Customer with id: %s not found", command.customerId()));

        customer.addAddress(Address.createNew(
                command.street(),
                command.streetNumber(),
                command.flatNumber(),
                command.city(),
                command.postalCode()
        ));

        customerRepository.save(customer);
    }
}
