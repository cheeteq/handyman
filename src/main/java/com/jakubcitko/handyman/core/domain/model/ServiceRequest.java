package com.jakubcitko.handyman.core.domain.model;

import com.jakubcitko.handyman.core.domain.exception.InvalidRequestStateException;
import com.jakubcitko.handyman.core.domain.exception.TimeSlotMismatchException;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class ServiceRequest {
    private final UUID id;
    private final String title;
    private final String description;
    private final UUID customerId;
    private final UUID addressId;
    private final List<UUID> attachments;
    private ServiceRequestStatus status;
    private Offer offer;
    private TimeSlot chosenTimeSlot;
    private BigDecimal revenue;
    private BigDecimal costOfParts;
    private String note;

    private ServiceRequest(UUID id, String title, String description, UUID customerId, UUID addressId, List<UUID> attachments) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.customerId = customerId;
        this.addressId = addressId;
        this.attachments = attachments;
        this.status = ServiceRequestStatus.NEW;
    }

    public static ServiceRequest createNew(String title, String description, UUID customerId, UUID addressId, List<UUID> attachments) {
        return new ServiceRequest(UUID.randomUUID(), title, description, customerId, addressId, attachments);
    }

    public void prepareOffer(BigDecimal estimatedCost, List<TimeSlot> availableTimeSlots) {
        if (!status.equals(ServiceRequestStatus.NEW))
            throw new InvalidRequestStateException("Cannot prepare an offer, invalid status: " + status + ". Expected: NEW.");

        this.offer = new Offer(estimatedCost, availableTimeSlots);
        this.status = ServiceRequestStatus.OFFER_CREATED;
    }

    public void rejectRequest(String note) {
        if (!status.equals(ServiceRequestStatus.NEW))
            throw new InvalidRequestStateException("Cannot reject a request, invalid status: " + status + ". Expected: NEW.");

        this.note = note;
        this.status = ServiceRequestStatus.REJECTED;
    }

    public void acceptOffer(TimeSlot chosenTimeSlot) {
        if (!status.equals(ServiceRequestStatus.OFFER_CREATED))
            throw new InvalidRequestStateException("Cannot accept an offer, invalid status: " + status + ". Expected: OFFER_CREATED.");

        if (this.offer == null)
            throw new IllegalStateException("Cannot accept offer when no offer has been prepared.");

        boolean isSlotAvailable = this.offer.getAvailableTimeSlots().contains(chosenTimeSlot);
        if (!isSlotAvailable)
            throw new TimeSlotMismatchException("The chosen time slot is not one of the available options.");

        this.chosenTimeSlot = chosenTimeSlot;
        this.status = ServiceRequestStatus.OFFER_ACCEPTED;
    }

    public void rejectOffer() {
        if (!status.equals(ServiceRequestStatus.OFFER_CREATED))
            throw new IllegalStateException();

        this.status = ServiceRequestStatus.OFFER_REJECTED;
    }

    public void cancel() {
        this.status = ServiceRequestStatus.CANCELED;
    }

    public void settle(BigDecimal finalRevenue, BigDecimal costsOfParts) {
        if (status != ServiceRequestStatus.OFFER_ACCEPTED) {
            throw new InvalidRequestStateException("Cannot settle a request. Invalid status: " + status + ". Expected: OFFER_ACCEPTED.");
        }
        this.revenue = finalRevenue;
        this.costOfParts = costsOfParts;
        this.status = ServiceRequestStatus.COMPLETED;
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public UUID getAddressId() {
        return addressId;
    }

    public List<UUID> getAttachments() {
        return attachments;
    }

    public ServiceRequestStatus getStatus() {
        return status;
    }

    public Offer getOffer() {
        return offer;
    }

    public TimeSlot getChosenTimeSlot() {
        return chosenTimeSlot;
    }

    public BigDecimal getRevenue() {
        return revenue;
    }

    public BigDecimal getCostOfParts() {
        return costOfParts;
    }

    public String getNote() {
        return note;
    }

}
