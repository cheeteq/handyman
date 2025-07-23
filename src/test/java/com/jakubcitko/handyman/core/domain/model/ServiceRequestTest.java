package com.jakubcitko.handyman.core.domain.model;

import com.jakubcitko.handyman.core.domain.exception.InvalidRequestStateException;
import com.jakubcitko.handyman.core.domain.exception.TimeSlotMismatchException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


public class ServiceRequestTest {

    private final ZoneOffset zoneOffset = ZoneOffset.UTC;

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
        assertEquals(ServiceRequestStatus.NEW, createdRequest.getStatus());
    }

    @Test
    void should_changeStatusAndOfferNotEmpty_when_prepareOfferCalled() {
        //GIVEN
        ServiceRequest serviceRequest = prepareDefaultServiceRequest();
        BigDecimal estimatedCost = BigDecimal.valueOf(150.00);
        List<TimeSlot> availableTimeSlots = List.of(
                new TimeSlot(
                        LocalDateTime.of(2025, 7, 18, 8, 0).toInstant(zoneOffset),
                        LocalDateTime.of(2025, 7, 18, 16, 0).toInstant(zoneOffset)
                )
        );

        //WHEN
        serviceRequest.prepareOffer(
                estimatedCost,
                availableTimeSlots
        );


        //THEN
        assertEquals(ServiceRequestStatus.OFFER_CREATED, serviceRequest.getStatus());
        assertNotNull(serviceRequest.getOffer());
        assertEquals(estimatedCost, serviceRequest.getOffer().getEstimatedCost());
        assertEquals(serviceRequest.getOffer().getAvailableTimeSlots(), availableTimeSlots);

    }

    @Test
    void should_throwInvalidRequestStateException_when_prepareOfferCalled_wrongStatus() {
        //GIVEN
        ServiceRequest serviceRequest = prepareDefaultServiceRequest();
        serviceRequest.prepareOffer(null, new ArrayList<>());
        assertEquals(ServiceRequestStatus.OFFER_CREATED, serviceRequest.getStatus());

        //THEN
        assertThrows(InvalidRequestStateException.class, () -> serviceRequest.prepareOffer(null, new ArrayList<>()));
    }

    @Test
    void should_changeStatus_when_rejectRequestCalled() {
        //GIVEN
        String note = "test_note_reject_reason";
        ServiceRequest serviceRequest = prepareDefaultServiceRequest();

        //WHEN
        serviceRequest.rejectRequest(note);

        //THEN
        assertEquals(ServiceRequestStatus.REJECTED, serviceRequest.getStatus());
        assertEquals(note, serviceRequest.getNote());
    }


    @Test
    void should_throwInvalidRequestStateException_when_rejectRequestCalled_wrongStatus() {
        //GIVEN
        ServiceRequest serviceRequest = prepareDefaultServiceRequest();
        serviceRequest.prepareOffer(null, new ArrayList<>());
        assertEquals(ServiceRequestStatus.OFFER_CREATED, serviceRequest.getStatus());

        //THEN
        assertThrows(InvalidRequestStateException.class, () -> serviceRequest.rejectRequest("note"));
    }

    @Test
    void should_changeStatusAndChosenTimeSlotNotEmpty_when_acceptOfferCalled() {
        //GIVEN
        TimeSlot chosenTimeSlot = new TimeSlot(
                LocalDateTime.of(2025, 7, 18, 8, 0).toInstant(zoneOffset),
                LocalDateTime.of(2025, 7, 18, 16, 0).toInstant(zoneOffset)
        );
        ServiceRequest serviceRequest = prepareDefaultServiceRequest();
        serviceRequest.prepareOffer(
                BigDecimal.valueOf(150.00),
                List.of(
                        new TimeSlot(
                                LocalDateTime.of(2025, 7, 18, 8, 0).toInstant(zoneOffset),
                                LocalDateTime.of(2025, 7, 18, 16, 0).toInstant(zoneOffset)
                        )
                )
        );

        //WHEN
        serviceRequest.acceptOffer(chosenTimeSlot);

        //THEN
        assertEquals(ServiceRequestStatus.OFFER_ACCEPTED, serviceRequest.getStatus());
        assertEquals(chosenTimeSlot, serviceRequest.getChosenTimeSlot());
    }

    @Test
    void should_throwInvalidRequestStateException_when_acceptOfferCalled_wrongStatus() {
        //GIVEN
        ServiceRequest serviceRequest = prepareDefaultServiceRequest();
        assertEquals(ServiceRequestStatus.NEW, serviceRequest.getStatus());

        //THEN
        assertThrows(InvalidRequestStateException.class, () -> serviceRequest.acceptOffer(new TimeSlot(
                LocalDateTime.of(2025, 7, 18, 8, 0).toInstant(zoneOffset),
                LocalDateTime.of(2025, 7, 18, 16, 0).toInstant(zoneOffset)
        )));
    }

    @Test
    void should_throwInvalidRequestStateException_when_acceptOfferCalled_wrongTimeSlot() {
        //GIVEN
        TimeSlot chosenTimeSlot = new TimeSlot(
                LocalDateTime.of(3000, 1, 1, 0, 0).toInstant(zoneOffset),
                LocalDateTime.of(3000, 1, 1, 0, 0).toInstant(zoneOffset)
        );
        ServiceRequest serviceRequest = prepareDefaultServiceRequest();
        serviceRequest.prepareOffer(
                BigDecimal.valueOf(150.00),
                List.of(
                        new TimeSlot(
                                LocalDateTime.of(2025, 7, 18, 8, 0).toInstant(zoneOffset),
                                LocalDateTime.of(2025, 7, 18, 16, 0).toInstant(zoneOffset)
                        )
                )
        );

        //THEN
        assertThrows(TimeSlotMismatchException.class, () -> serviceRequest.acceptOffer(chosenTimeSlot));
    }

    @Test
    void should_changeStatus_when_rejectOfferCalled() {
        //GIVEN
        ServiceRequest serviceRequest = prepareDefaultServiceRequest();
        serviceRequest.prepareOffer(null, new ArrayList<>());

        //WHEN
        serviceRequest.rejectOffer();

        //THEN
        assertEquals(ServiceRequestStatus.OFFER_REJECTED, serviceRequest.getStatus());
    }

    @Test
    void should_changeStatus_when_cancelCalled() {
        //GIVEN
        ServiceRequest serviceRequest = prepareDefaultServiceRequest();
        serviceRequest.prepareOffer(null, new ArrayList<>());

        //WHEN
        serviceRequest.cancel();

        //THEN
        assertEquals(ServiceRequestStatus.CANCELED, serviceRequest.getStatus());
    }

    @Test
    void should_changeStatusAndRevenueAndCostsNotEmpty_when_settleCalled() {
        //GIVEN
        ServiceRequest serviceRequest = prepareDefaultServiceRequest();
        serviceRequest.prepareOffer(
                BigDecimal.valueOf(150.00),
                List.of(
                        new TimeSlot(
                                LocalDateTime.of(2025, 7, 18, 8, 0).toInstant(zoneOffset),
                                LocalDateTime.of(2025, 7, 18, 16, 0).toInstant(zoneOffset)
                        )
                )
        );
        serviceRequest.acceptOffer(new TimeSlot(
                LocalDateTime.of(2025, 7, 18, 8, 0).toInstant(zoneOffset),
                LocalDateTime.of(2025, 7, 18, 16, 0).toInstant(zoneOffset)
        ));
        BigDecimal finalRevenue = BigDecimal.valueOf(400);
        BigDecimal costsOfParts = BigDecimal.valueOf(30);

        //WHEN
        serviceRequest.settle(finalRevenue, costsOfParts);

        //THEN
        assertEquals(ServiceRequestStatus.COMPLETED, serviceRequest.getStatus());
        assertEquals(finalRevenue, serviceRequest.getRevenue());
        assertEquals(costsOfParts, serviceRequest.getCostOfParts());
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
