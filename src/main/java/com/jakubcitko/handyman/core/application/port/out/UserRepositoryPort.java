package com.jakubcitko.handyman.core.application.port.out;

import com.jakubcitko.handyman.core.domain.model.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepositoryPort {
    void save(User user);
    boolean existsByEmail(String email);
    Optional<User> findById(UUID id);
    Optional<User> findByEmail(String email);
}