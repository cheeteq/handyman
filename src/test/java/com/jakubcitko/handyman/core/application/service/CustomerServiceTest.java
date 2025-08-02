package com.jakubcitko.handyman.core.application.service;

import com.jakubcitko.handyman.adapters.inbound.web.dto.AddressResponseDto;
import com.jakubcitko.handyman.core.application.port.in.AddAddressToCustomerUseCase;
import com.jakubcitko.handyman.core.application.port.in.GetCustomerAddressesUseCase;
import com.jakubcitko.handyman.core.application.port.in.RegisterCustomerUseCase;
import com.jakubcitko.handyman.core.application.port.out.CustomerRepositoryPort;
import com.jakubcitko.handyman.core.application.port.out.UserRepositoryPort;
import com.jakubcitko.handyman.core.domain.exception.BusinessRuleViolationException;
import com.jakubcitko.handyman.core.domain.model.Address;
import com.jakubcitko.handyman.core.domain.model.Customer;
import com.jakubcitko.handyman.core.domain.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepositoryPort customerRepository;
    @Mock
    private UserRepositoryPort userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private CustomerService customerService;

    @Nested
    @DisplayName("RegisterCustomerUseCase Tests")
    class RegisterCustomerTests {

        @Test
        void should_registerUserAndCustomer_when_emailIsUnique() {
            // GIVEN
            var command = new RegisterCustomerUseCase.RegisterCustomerCommand(
                    "test@example.com", "password123", "Test User", "123456789");
            when(userRepository.existsByEmail(command.email())).thenReturn(false);
            when(passwordEncoder.encode(command.password())).thenReturn("hashedPassword");

            // WHEN
            customerService.registerCustomer(command);

            // THEN
            verify(userRepository, times(1)).save(any(User.class));
            verify(customerRepository, times(1)).save(any(Customer.class));
        }

        @Test
        void should_throwEmailAlreadyUsedException_when_emailExists() {
            // GIVEN
            var command = new RegisterCustomerUseCase.RegisterCustomerCommand(
                    "existing@example.com", "password123", "Test User", "123456789");
            when(userRepository.existsByEmail(command.email())).thenReturn(true);

            // WHEN & THEN
            assertThrows(BusinessRuleViolationException.class, () -> {
                customerService.registerCustomer(command);
            });

            verify(userRepository, never()).save(any());
            verify(customerRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("AddAddressToCustomerUseCase Tests")
    class AddAddressTests {

        @Test
        void should_addAddressAndUpdateCustomer_when_customerExists() {
            // GIVEN
            UUID customerId = UUID.randomUUID();
            var addressData = new AddAddressToCustomerUseCase.AddressData("Street", "1", null, "City", "12345");
            var command = new AddAddressToCustomerUseCase.AddAddressToCustomerCommand(customerId, addressData);

            Customer mockCustomer = Customer.registerNew(customerId, "Test User", "123456789");
            when(customerRepository.findById(customerId)).thenReturn(Optional.of(mockCustomer));

            // WHEN
            customerService.addAddressToCustomer(command);

            // THEN
            verify(customerRepository, times(1)).update(mockCustomer);

            assertEquals(1, mockCustomer.getAddresses().size());
            assertEquals("Street", mockCustomer.getAddresses().get(0).getStreet());
        }

        @Test
        void should_throwCustomerNotFoundException_when_customerDoesNotExist() {
            // GIVEN
            UUID nonExistentId = UUID.randomUUID();
            var command = new AddAddressToCustomerUseCase.AddAddressToCustomerCommand(nonExistentId, mock(AddAddressToCustomerUseCase.AddressData.class));
            when(customerRepository.findById(nonExistentId)).thenReturn(Optional.empty());

            // WHEN & THEN
            assertThrows(BusinessRuleViolationException.class, () -> {
                customerService.addAddressToCustomer(command);
            });
        }
    }

    @Nested
    @DisplayName("GetCustomerAddressesUseCase Tests")
    class GetAddressesTests {

        @Test
        void should_returnMappedAddressDtos_when_customerHasAddresses() {
            // GIVEN
            UUID customerId = UUID.randomUUID();
            var query = new GetCustomerAddressesUseCase.GetCustomerAddressesQuery(customerId);

            Address address1 = Address.createNew("Street 1", "1A", null, "City", "11111");
            Address address2 = Address.createNew("Street 2", "2B", "5", "City", "22222");
            when(customerRepository.findAddressesByCustomerId(customerId)).thenReturn(List.of(address1, address2));

            // WHEN
            List<AddressResponseDto> result = customerService.getAddressesForCustomer(query);

            // THEN
            assertNotNull(result);
            assertEquals(2, result.size());
            assertEquals("Street 1", result.get(0).street());
            assertEquals("2B", result.get(1).streetNumber());
        }

        @Test
        void should_returnEmptyList_when_customerHasNoAddresses() {
            // GIVEN
            UUID customerId = UUID.randomUUID();
            var query = new GetCustomerAddressesUseCase.GetCustomerAddressesQuery(customerId);
            when(customerRepository.findAddressesByCustomerId(customerId)).thenReturn(List.of());

            // WHEN
            List<AddressResponseDto> result = customerService.getAddressesForCustomer(query);

            // THEN
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }
    }
}