package com.jakubcitko.handyman.adapters.outbound.persistence.mapper;

import com.jakubcitko.handyman.adapters.outbound.persistence.entity.UserEntity;
import com.jakubcitko.handyman.core.domain.model.User;
import com.jakubcitko.handyman.core.domain.model.UserAccount;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserPersistenceMapper {
    UserAccount toUserAccount(UserEntity entity);

    UserEntity toEntity(User userDomainObject);

    User toDomain(UserEntity entity);
}