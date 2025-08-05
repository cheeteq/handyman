package com.jakubcitko.handyman.core.domain.model;

import com.jakubcitko.handyman.core.domain.exception.BusinessRuleViolationException;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
@Getter
public class ServiceRequest {
    private final UUID id;
    private final String title;
    private final String description;
    private final UUID customerId;
    private final UUID addressId;
    private final List<Attachment> attachments;
    private ServiceRequestStatus status;
    private Offer offer;
    private TimeSlot chosenTimeSlot;
    private BigDecimal revenue;
    private BigDecimal costOfParts;
    private String note;

    public ServiceRequest(UUID id, String title, String description, UUID customerId, UUID addressId, List<Attachment> attachments) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.customerId = customerId;
        this.addressId = addressId;
        this.attachments = attachments;
        this.status = ServiceRequestStatus.NEW;
    }

    public static ServiceRequest createNew(String title, String description, UUID customerId, UUID addressId) {
        return new ServiceRequest(UUID.randomUUID(), title, description, customerId, addressId, null);
    }

    public void prepareOffer(BigDecimal estimatedCost, List<TimeSlot> availableTimeSlots) {
        if (!status.equals(ServiceRequestStatus.NEW))
            throw new BusinessRuleViolationException("Cannot prepare an offer, invalid status: %s. Expected: NEW.", status);

        this.offer = new Offer(estimatedCost, availableTimeSlots);
        this.status = ServiceRequestStatus.OFFER_CREATED;
    }

    public void rejectRequest(String note) {
        if (!status.equals(ServiceRequestStatus.NEW))
            throw new BusinessRuleViolationException("Cannot reject a request, invalid status: %s. Expected: NEW.", status);

        this.note = note;
        this.status = ServiceRequestStatus.REJECTED;
    }

    public void acceptOffer(TimeSlot chosenTimeSlot) {
        if (!status.equals(ServiceRequestStatus.OFFER_CREATED))
            throw new BusinessRuleViolationException("Cannot accept an offer, invalid status: %s. Expected: OFFER_CREATED.", status);

        if (this.offer == null)
            throw new BusinessRuleViolationException("Cannot accept offer when no offer has been prepared.");

        boolean isSlotAvailable = this.offer.getAvailableTimeSlots().contains(chosenTimeSlot);
        if (!isSlotAvailable)
            throw new BusinessRuleViolationException("The chosen time slot is not one of the available options.");

        this.chosenTimeSlot = chosenTimeSlot;
        this.status = ServiceRequestStatus.OFFER_ACCEPTED;
    }

    public void rejectOffer() {
        if (!status.equals(ServiceRequestStatus.OFFER_CREATED))
            throw new BusinessRuleViolationException("Cannot reject an offer, invalid status: %s. Expected: OFFER_CREATED.", status);

        this.status = ServiceRequestStatus.OFFER_REJECTED;
    }

    public void cancel() {
        this.status = ServiceRequestStatus.CANCELED;
    }

    public void settle(BigDecimal finalRevenue, BigDecimal costsOfParts) {
        if (status != ServiceRequestStatus.OFFER_ACCEPTED) {
            throw new BusinessRuleViolationException("Cannot settle a request. Invalid status: %s. Expected: OFFER_ACCEPTED.", status);
        }
        this.revenue = finalRevenue;
        this.costOfParts = costsOfParts;
        this.status = ServiceRequestStatus.COMPLETED;
    }
}
