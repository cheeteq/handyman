package com.jakubcitko.handyman.adapters.outbound.persistence;

import com.jakubcitko.handyman.adapters.inbound.web.dto.CustomerRequestSummaryDto;
import com.jakubcitko.handyman.adapters.outbound.persistence.entity.ServiceRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ServiceRequestQueryJpaRepository extends JpaRepository<ServiceRequestEntity, UUID> {

    @Query("""
                SELECT new com.jakubcitko.handyman.adapters.inbound.web.dto.CustomerRequestSummaryDto(
                    sr.id,
                    sr.title,
                    CAST(sr.status AS string),
                    sr.creationDate
                )
                FROM ServiceRequestEntity sr
                WHERE sr.customerId = :customerId
                ORDER BY sr.creationDate DESC
            """)
    List<CustomerRequestSummaryDto> findSummariesByCustomerId(@Param("customerId") UUID customerId);
}
