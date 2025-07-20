package com.jakubcitko.handyman.core.application.service;

import com.jakubcitko.handyman.core.application.port.in.AddAddressToCustomerUseCase;
import com.jakubcitko.handyman.core.application.port.in.RegisterCustomerUseCase;
import com.jakubcitko.handyman.core.application.port.out.CustomerRepositoryPort;
import com.jakubcitko.handyman.core.application.port.out.UserRepositoryPort;
import com.jakubcitko.handyman.core.domain.exception.CustomerNotFoundException;
import com.jakubcitko.handyman.core.domain.exception.EmailAlreadyUsedException;
import com.jakubcitko.handyman.core.domain.model.Address;
import com.jakubcitko.handyman.core.domain.model.Customer;
import com.jakubcitko.handyman.core.domain.model.Role;
import com.jakubcitko.handyman.core.domain.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@Transactional
public class CustomerService implements RegisterCustomerUseCase, AddAddressToCustomerUseCase {

    private final CustomerRepositoryPort customerRepository;
    private final UserRepositoryPort userRepository;
    private final PasswordEncoder passwordEncoder;

    public CustomerService(CustomerRepositoryPort customerRepository, UserRepositoryPort userRepository, PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void registerCustomer(RegisterCustomerCommand command) {
        if (userRepository.existsByEmail(command.email())) {
            throw new EmailAlreadyUsedException("Email: %s is already in use.", command.email());
        }

        String hashedPassword = passwordEncoder.encode(command.password());
        User newUser = User.register(
                command.email(),
                hashedPassword,
                Set.of(Role.ROLE_CUSTOMER)
        );

        userRepository.save(newUser);

        Customer newCustomer = Customer.registerNew(
                newUser.getId(),
                command.displayName(),
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
