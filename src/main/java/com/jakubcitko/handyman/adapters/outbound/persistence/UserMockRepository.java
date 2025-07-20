package com.jakubcitko.handyman.adapters.outbound.persistence;

import com.jakubcitko.handyman.core.application.port.out.UserRepositoryPort;
import com.jakubcitko.handyman.core.domain.model.User;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
@Repository
@Profile({"mock"})
public class UserMockRepository implements UserRepositoryPort {
    private final Map<UUID, User> users = new HashMap();
    @Override
    public void save(User user) {
        users.put(user.getId(), user);
    }

    @Override
    public boolean existsByEmail(String email) {
        return users.values().stream()
                .anyMatch(user -> user.getEmail().equalsIgnoreCase(email));
    }

    @Override
    public Optional<User> findById(UUID id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return users.values().stream()
                .filter(user -> user.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }
}
