package com.jakubcitko.handyman.adapters.outbound.persistence.entity;

import com.jakubcitko.handyman.core.domain.model.ServiceRequestStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "service_requests")
@Getter
@Setter
public class ServiceRequestEntity {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description_text", columnDefinition = "TEXT")
    private String description;

    @Column(name = "customer_id", nullable = false)
    private UUID customerId;

    @Column(name = "address_id", nullable = false)
    private UUID addressId;
    @Embedded
    private OfferEmbeddable offer;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "startDateTime", column = @Column(name = "chosen_slot_start")),
            @AttributeOverride(name = "endDateTime", column = @Column(name = "chosen_slot_end"))
    })
    private TimeSlotEmbeddable chosenTimeSlot;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "service_request_attachments", joinColumns = @JoinColumn(name = "service_request_id"))
    @Column(name = "attachment_id")
    private List<UUID> attachments;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ServiceRequestStatus status;

    @Column(name = "final_revenue")
    private BigDecimal revenue;

    @Column(name = "costs_of_parts")
    private BigDecimal costs;

    @Column(name = "internal_note", columnDefinition = "TEXT")
    private String note;

    @Version
    private Long version;
}