package com.jakubcitko.handyman.core.domain.model;

import com.jakubcitko.handyman.core.domain.exception.InvalidRequestStateException;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class ServiceRequest {
    private final UUID uuid;
    private final String title;
    private final String description;
    private final Address address;
    private final Customer customer;
    private final List<FileReference> attachments;
    private ServiceRequestStatusEnum status;
    private Offer offer;
    private TimeSlot chosenTimeSlot;
    private BigDecimal revenue;
    private BigDecimal costs;
    private String note;

    private ServiceRequest(UUID uuid, String title, String description, Address address, Customer customer, List<FileReference> attachments) {
        this.uuid = uuid;
        this.title = title;
        this.description = description;
        this.address = address;
        this.customer = customer;
        this.attachments = attachments;
        this.status = ServiceRequestStatusEnum.NEW;
    }

    public static ServiceRequest createNew(String title, String description, Address address, Customer customer, List<FileReference> attachments) {
        return new ServiceRequest(UUID.randomUUID(), title, description, address, customer, attachments);
    }

    public void prepareOffer(BigDecimal estimatedCost, List<TimeSlot> availableTimeSlots) {
        if(!status.equals(ServiceRequestStatusEnum.NEW))
            throw new InvalidRequestStateException("Cannot prepare an offer, invalid status: " + status + ". Expected: NEW.");

        this.offer = new Offer(estimatedCost, availableTimeSlots);
        this.status = ServiceRequestStatusEnum.OFFER_CREATED;
    }

    public void rejectRequest(String note) {
        if(!status.equals(ServiceRequestStatusEnum.NEW))
            throw new InvalidRequestStateException("Cannot reject a request, invalid status: " + status + ". Expected: NEW.");

        this.note = note;
        this.status = ServiceRequestStatusEnum.REJECTED;
    }

    public void acceptOffer(TimeSlot chosenTimeSlot) {
        if(!status.equals(ServiceRequestStatusEnum.OFFER_CREATED))
            throw new InvalidRequestStateException("Cannot accept an offer, invalid status: " + status + ". Expected: OFFER_CREATED.");

        this.chosenTimeSlot = chosenTimeSlot;
        this.status = ServiceRequestStatusEnum.OFFER_ACCEPTED;
    }

    public void rejectOffer() {
        if(!status.equals(ServiceRequestStatusEnum.OFFER_CREATED))
            throw new IllegalStateException();

        this.status = ServiceRequestStatusEnum.OFFER_REJECTED;
    }

    public void cancel() {
        this.status = ServiceRequestStatusEnum.CANCELED;
    }

    public void settle(BigDecimal finalRevenue, BigDecimal costsOfParts) {
        if (status != ServiceRequestStatusEnum.OFFER_ACCEPTED) {
            throw new InvalidRequestStateException("Cannot settle a request. Invalid status: " + status + ". Expected: OFFER_ACCEPTED.");
        }
        this.revenue = finalRevenue;
        this.costs = costsOfParts;
        this.status = ServiceRequestStatusEnum.COMPLETED;
    }
}
