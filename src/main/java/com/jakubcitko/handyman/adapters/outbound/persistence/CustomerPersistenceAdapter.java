package com.jakubcitko.handyman.adapters.outbound.persistence;

import com.jakubcitko.handyman.adapters.outbound.persistence.entity.CustomerEntity;
import com.jakubcitko.handyman.adapters.outbound.persistence.entity.UserEntity;
import com.jakubcitko.handyman.adapters.outbound.persistence.mapper.CustomerPersistenceMapper;
import com.jakubcitko.handyman.core.application.port.out.CustomerRepositoryPort;
import com.jakubcitko.handyman.core.domain.model.Address;
import com.jakubcitko.handyman.core.domain.model.Customer;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@Profile({"test", "dev"})
public class CustomerPersistenceAdapter implements CustomerRepositoryPort {
    private final CustomerJpaRepository customerJpaRepository;
    private final CustomerPersistenceMapper mapper;

    public CustomerPersistenceAdapter(
            CustomerJpaRepository customerJpaRepository,
            CustomerPersistenceMapper mapper
    ) {
        this.customerJpaRepository = customerJpaRepository;
        this.mapper = mapper;
    }

    @Override
    public void save(Customer customer) {
        CustomerEntity customerEntity = mapper.toEntity(customer);
        customerJpaRepository.save(customerEntity);
    }

    @Override
    public void update(Customer customer) {
        CustomerEntity entityToUpdate = customerJpaRepository.findById(customer.getId())
                .orElseThrow(() -> new EntityNotFoundException("Cannot update non-existent customer"));
        mapper.updateEntityFromDomain(customer, entityToUpdate);
        customerJpaRepository.save(entityToUpdate);
    }

    @Override
    public Optional<Customer> findById(UUID id) {
        Optional<CustomerEntity> entityOptional = customerJpaRepository.findById(id);
        return entityOptional.map(mapper::toDomain);
    }

    @Override
    public List<Address> findAddressesByCustomerId(UUID customerId) {
        return customerJpaRepository.findById(customerId)
                .map(customerEntity -> customerEntity.getAddresses().stream()
                        .map(mapper::toAddressDomain)
                        .collect(Collectors.toList()))
                .orElse(new ArrayList<>());
    }
}