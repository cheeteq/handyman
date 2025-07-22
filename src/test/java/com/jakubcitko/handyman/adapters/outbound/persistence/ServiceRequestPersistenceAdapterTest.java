package com.jakubcitko.handyman.adapters.outbound.persistence;

import com.jakubcitko.handyman.AbstractSpringBootTest;
import com.jakubcitko.handyman.core.domain.model.ServiceRequest;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ServiceRequestPersistenceAdapterTest extends AbstractSpringBootTest {

    @Autowired
    ServiceRequestPersistenceAdapter persistenceAdapter;

    @Test
    void test_saveServiceRequest() {
        //GIVEN
        String title = "title_test";
        String description = "description_test";
        UUID customerId = UUID.randomUUID();
        UUID addressId = UUID.randomUUID();
        List<UUID> attachmentList = List.of(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());
        ServiceRequest serviceRequest = ServiceRequest.createNew(title, description, customerId, addressId, attachmentList);
        UUID id = serviceRequest.getId();

        //WHEN
        persistenceAdapter.save(serviceRequest);

        //THEN
        ServiceRequest savedServiceRequest = persistenceAdapter.findById(id).orElseThrow();
        assertEquals(title, savedServiceRequest.getTitle());
        assertEquals(description, savedServiceRequest.getDescription());
        assertEquals(customerId, savedServiceRequest.getCustomerId());

        assertThat(savedServiceRequest.getAttachments())
                .containsExactlyElementsOf(attachmentList);
    }
}