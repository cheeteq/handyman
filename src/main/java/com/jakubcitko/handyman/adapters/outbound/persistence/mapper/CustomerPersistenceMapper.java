package com.jakubcitko.handyman.adapters.outbound.persistence.mapper;

import com.jakubcitko.handyman.adapters.outbound.persistence.entity.AddressEntity;
import com.jakubcitko.handyman.adapters.outbound.persistence.entity.CustomerEntity;
import com.jakubcitko.handyman.core.domain.model.Address;
import com.jakubcitko.handyman.core.domain.model.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CustomerPersistenceMapper {
    CustomerEntity toEntity(Customer customer);

    AddressEntity toAddressEntity(Address address);

    void updateEntityFromDomain(Customer customer, @MappingTarget CustomerEntity customerEntity);

    Customer toDomain(CustomerEntity entity);

    Address toAddressDomain(AddressEntity addressEntity);
}