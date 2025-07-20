package com.jakubcitko.handyman.adapters.outbound.persistence;

import com.jakubcitko.handyman.adapters.outbound.persistence.mapper.UserPersistenceMapper;
import com.jakubcitko.handyman.core.application.port.out.LoadUserAccountPort;
import com.jakubcitko.handyman.core.application.port.out.UserRepositoryPort;
import com.jakubcitko.handyman.core.domain.model.User;
import com.jakubcitko.handyman.core.domain.model.UserAccount;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@Profile({"test", "dev"})
public class UserPersistenceAdapter implements UserRepositoryPort, LoadUserAccountPort {

    private final UserJpaRepository userJpaRepository;
    private final UserPersistenceMapper mapper;

    public UserPersistenceAdapter(UserJpaRepository userJpaRepository, UserPersistenceMapper mapper) {
        this.userJpaRepository = userJpaRepository;
        this.mapper = mapper;
    }


    @Override
    public void save(User user) {
        var userEntity = mapper.toEntity(user);
        userJpaRepository.save(userEntity);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userJpaRepository.existsByEmail(email);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return userJpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userJpaRepository.findByEmail(email)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<UserAccount> loadUserByEmail(String email) {
        return userJpaRepository.findByEmail(email)
                .map(mapper::toUserAccount);
    }
}