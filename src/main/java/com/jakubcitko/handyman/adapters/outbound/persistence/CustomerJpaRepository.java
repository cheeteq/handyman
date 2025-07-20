package com.jakubcitko.handyman.adapters.outbound.persistence;

import com.jakubcitko.handyman.adapters.outbound.persistence.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CustomerJpaRepository extends JpaRepository<CustomerEntity, UUID> {
}