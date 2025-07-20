package com.jakubcitko.handyman.adapters.outbound.persistence;

import com.jakubcitko.handyman.adapters.outbound.persistence.entity.ServiceRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface ServiceRequestJpaRepository extends JpaRepository<ServiceRequestEntity, UUID> {
}
