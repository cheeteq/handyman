package com.jakubcitko.handyman.adapters.outbound.persistence.mapper;

import com.jakubcitko.handyman.adapters.outbound.persistence.entity.OfferEmbeddable;
import com.jakubcitko.handyman.adapters.outbound.persistence.entity.ServiceRequestEntity;
import com.jakubcitko.handyman.adapters.outbound.persistence.entity.TimeSlotEmbeddable;
import com.jakubcitko.handyman.core.domain.model.Offer;
import com.jakubcitko.handyman.core.domain.model.ServiceRequest;
import com.jakubcitko.handyman.core.domain.model.TimeSlot;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface ServiceRequestPersistenceMapper {
    ServiceRequestEntity toEntity(ServiceRequest domainObject);

    ServiceRequest toDomain(ServiceRequestEntity entity);

    TimeSlotEmbeddable toTimeSlotEmbeddable(TimeSlot timeSlot);

    TimeSlot toTimeSlotDomain(TimeSlotEmbeddable timeSlotEmbeddable);

    OfferEmbeddable toOfferEmbeddable(Offer offer);

    Offer toOfferDomain(OfferEmbeddable offerEmbeddable);
}