package com.jakubcitko.handyman.core.application.service;

import com.jakubcitko.handyman.adapters.inbound.web.dto.AddressResponseDto;
import com.jakubcitko.handyman.core.application.port.in.AddAddressToCustomerUseCase;
import com.jakubcitko.handyman.core.application.port.in.GetCustomerAddressesUseCase;
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

import java.util.List;
import java.util.Set;

@Service
@Transactional
public class CustomerService implements RegisterCustomerUseCase, AddAddressToCustomerUseCase, GetCustomerAddressesUseCase {

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
                command.addressData().street(),
                command.addressData().streetNumber(),
                command.addressData().flatNumber(),
                command.addressData().city(),
                command.addressData().postalCode()
        ));

        customerRepository.update(customer);
    }

    @Override
    public List<AddressResponseDto> getAddressesForCustomer(GetCustomerAddressesQuery query) {
        List<Address> addresses = customerRepository.findAddressesByCustomerId(query.customerId());
        return addresses.stream()
                .map(this::mapAddressToDto)
                .toList();
    }

    private AddressResponseDto mapAddressToDto(Address address) {
        return new AddressResponseDto(
                address.getId(),
                address.getStreet(),
                address.getStreetNumber(),
                address.getFlatNumber(),
                address.getCity(),
                address.getPostalCode()
        );
    }
}
