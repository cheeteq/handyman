package com.jakubcitko.handyman.adapters.outbound.persistence;

import com.jakubcitko.handyman.AbstractSpringBootTest;
import com.jakubcitko.handyman.core.domain.model.Address;
import com.jakubcitko.handyman.core.domain.model.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


class CustomerPersistenceAdapterTest extends AbstractSpringBootTest {
    @Autowired
    CustomerPersistenceAdapter persistenceAdapter;

    @Test
    void test_saveCustomer() {
        //GIVEN
        UUID id = UUID.randomUUID();
        String displayName = "test_name";
        String phoneNumber = "000-000-000";
        Customer customer = Customer.registerNew(id, displayName, phoneNumber);

        //WHEN
        persistenceAdapter.save(customer);

        //THEN
        Customer savedCustomer = persistenceAdapter.findById(id).orElseThrow();
        assertEquals(displayName, savedCustomer.getDisplayName());
        assertEquals(phoneNumber, savedCustomer.getPhoneNumber());
    }

    @Test
    void test_updateCustomer_addressAdded() {
        //GIVEN
        UUID id = UUID.randomUUID();
        String displayName = "test_name";
        String phoneNumber = "000-000-000";
        Customer customer = Customer.registerNew(id, displayName, phoneNumber);
        persistenceAdapter.save(customer);

        String street = "street_name";
        String streetNumber = "1";
        String flatNumber = "2";
        String city = "city_name";
        String postalCode = "00-000";

        //WHEN
        customer.addAddress(Address.createNew(street, streetNumber, flatNumber, city, postalCode));
        persistenceAdapter.update(customer);

        //THEN
        Customer updatedCustomer = persistenceAdapter.findById(id).orElseThrow();
        assertFalse(updatedCustomer.getAddresses().isEmpty());

        Address address = persistenceAdapter.findAddressesByCustomerId(id).getFirst();

        assertNotNull(address);
        assertEquals(street, address.getStreet());
        assertEquals(flatNumber, address.getFlatNumber());
        assertEquals(city, address.getCity());
        assertEquals(postalCode, address.getPostalCode());
    }

}