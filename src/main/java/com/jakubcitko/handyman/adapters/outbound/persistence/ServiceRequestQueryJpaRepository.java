package com.jakubcitko.handyman.adapters.outbound.persistence;

import com.jakubcitko.handyman.adapters.inbound.web.dto.response.AttachmentDetailsResponseDto;
import com.jakubcitko.handyman.adapters.inbound.web.dto.response.CustomerRequestDetailsResponseDto;
import com.jakubcitko.handyman.adapters.inbound.web.dto.response.CustomerRequestSummaryResponseDto;
import com.jakubcitko.handyman.adapters.outbound.persistence.entity.ServiceRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ServiceRequestQueryJpaRepository extends JpaRepository<ServiceRequestEntity, UUID> {

    @Query("""
                SELECT new com.jakubcitko.handyman.adapters.inbound.web.dto.response.CustomerRequestSummaryResponseDto(
                    sr.id,
                    sr.title,
                    CONCAT(a.street, ' ', a.streetNumber,
                            CASE WHEN a.flatNumber IS NOT NULL
                            THEN CONCAT('/', a.flatNumber) 
                            ELSE ''
                            END,
                         ', ', a.city, ' ', a.postalCode),
                    CAST(sr.status AS string),
                    sr.creationDate
                )
                FROM ServiceRequestEntity sr
                JOIN AddressEntity a ON sr.addressId = a.id
                
                WHERE sr.customerId = :customerId
                ORDER BY sr.creationDate DESC
            """)
    List<CustomerRequestSummaryResponseDto> findSummariesByCustomerId(@Param("customerId") UUID customerId);



    @Query("""
        SELECT new com.jakubcitko.handyman.adapters.inbound.web.dto.response.CustomerRequestDetailsResponseDto(
            sr.id,
            sr.title,
            sr.description,
            CAST(sr.status AS string),
            c.id,
            c.displayName,
            c.phoneNumber,
            CONCAT(a.street, ' ', a.streetNumber,
                            CASE WHEN a.flatNumber IS NOT NULL
                            THEN CONCAT('/', a.flatNumber) 
                            ELSE ''
                            END,
                         ', ', a.city, ' ', a.postalCode),
            sr.offer.estimatedCost,
            sr.chosenTimeSlot.startDateTime,
            sr.chosenTimeSlot.endDateTime
        )
        FROM ServiceRequestEntity sr
        JOIN CustomerEntity c ON sr.customerId = c.id
        JOIN AddressEntity a ON sr.addressId = a.id
        WHERE sr.id = :serviceRequestId
    """)
    Optional<CustomerRequestDetailsResponseDto> findCustomerRequestDetailsById(@Param("serviceRequestId") UUID serviceRequestId);

    @Query("""
        SELECT new com.jakubcitko.handyman.adapters.inbound.web.dto.response.AttachmentDetailsResponseDto(
            a.id,
            a.originalFilename
        )
        FROM AttachmentEntity a
        WHERE a.serviceRequestId = :serviceRequestId
    """)
    List<AttachmentDetailsResponseDto> findAttachmentsByServiceRequestId(@Param("serviceRequestId") UUID serviceRequestId);

}
