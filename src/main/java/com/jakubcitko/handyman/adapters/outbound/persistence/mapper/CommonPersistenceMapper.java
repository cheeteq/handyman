package com.jakubcitko.handyman.adapters.outbound.persistence.mapper;

import com.jakubcitko.handyman.adapters.outbound.persistence.entity.OfferEmbeddable;
import com.jakubcitko.handyman.adapters.outbound.persistence.entity.TimeSlotEmbeddable;
import com.jakubcitko.handyman.core.domain.model.Offer;
import com.jakubcitko.handyman.core.domain.model.TimeSlot;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface CommonPersistenceMapper {
    TimeSlotEmbeddable toTimeSlotEmbeddable(TimeSlot timeSlot);

    TimeSlot toTimeSlotDomain(TimeSlotEmbeddable timeSlotEmbeddable);

    OfferEmbeddable toOfferEmbeddable(Offer offer);

    Offer toOfferDomain(OfferEmbeddable offerEmbeddable);
}