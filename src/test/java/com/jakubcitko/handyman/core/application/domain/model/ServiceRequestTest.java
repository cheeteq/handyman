package com.jakubcitko.handyman.core.application.domain.model;

import com.jakubcitko.handyman.core.domain.model.ServiceRequest;
import com.jakubcitko.handyman.core.domain.model.ServiceRequestStatusEnum;
import com.jakubcitko.handyman.core.domain.model.TimeSlot;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


public class ServiceRequestTest {

    @Test
    void should_createNewRequestServiceWithId_when_factoryMethodCalled() {
        //GIVEN
        String title = "test_title";
        String description = "test_description";
        UUID customerId = UUID.randomUUID();
        UUID addressId = UUID.randomUUID();
        List<UUID> attachments = List.of(UUID.randomUUID(), UUID.randomUUID());


        //WHEN
        ServiceRequest createdRequest = ServiceRequest.createNew(
                title,
                description,
                customerId,
                addressId,
                attachments
        );

        //THEN
        assertNotNull(createdRequest);
        assertNotNull(createdRequest.getId());
        assertEquals(title, createdRequest.getTitle());
        assertEquals(description, createdRequest.getDescription());
        assertEquals(customerId, createdRequest.getCustomerId());
        assertEquals(addressId, createdRequest.getAddressId());
        assertEquals(attachments, createdRequest.getAttachments());
        assertEquals(ServiceRequestStatusEnum.NEW, createdRequest.getStatus());
    }

    @Test
    void should_changeStatusAndOfferNotEmpty_when_prepareOfferCalled() {
        //GIVEN
        ServiceRequest serviceRequest = prepareDefaultServiceRequest();
        BigDecimal estimatedCost = BigDecimal.valueOf(150.00);
        List<TimeSlot> availableTimeSlots = List.of(
                new TimeSlot(
                        LocalDateTime.of(2025, 7, 18, 8,0),
                        LocalDateTime.of(2025, 7, 18, 16,0)
                )
        );

        //WHEN
        serviceRequest.prepareOffer(
                BigDecimal.valueOf(150.00),
                availableTimeSlots
        );


        //THEN
        assertEquals(ServiceRequestStatusEnum.OFFER_CREATED, serviceRequest.getStatus());
        assertNotNull(serviceRequest.getOffer());
        assertEquals(estimatedCost, serviceRequest.getOffer().getEstimatedCost());
        assertEquals(serviceRequest.getOffer().getAvailableTimeSlots(), availableTimeSlots);

    }

    private ServiceRequest prepareDefaultServiceRequest() {
        return ServiceRequest.createNew(
                "test_title",
                "test_description",
                UUID.randomUUID(),
                UUID.randomUUID(),
                List.of(UUID.randomUUID())
        );
    }
}
