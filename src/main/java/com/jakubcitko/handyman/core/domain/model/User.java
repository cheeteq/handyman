package com.jakubcitko.handyman.core.domain.model;

import lombok.Getter;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Getter
public class User {
    private final UUID id;
    private final String email;
    private final String passwordHash;
    private final Set<Role> roles;

    public User(UUID id, String email, String passwordHash, Set<Role> roles) {
        Objects.requireNonNull(id, "User ID cannot be null");
        Objects.requireNonNull(email, "Email cannot be null");
        Objects.requireNonNull(roles, "Roles set cannot be null");

        this.id = id;
        this.email = email;
        this.passwordHash = passwordHash;
        this.roles = roles;
    }

    public static User register(String email, String hashedPassword, Set<Role> roles) {
        if (hashedPassword == null || hashedPassword.isBlank()) {
            throw new IllegalArgumentException("Password hash cannot be empty");
        }
        if (roles == null || roles.isEmpty()) {
            throw new IllegalArgumentException("User must have at least one role.");
        }
        return new User(UUID.randomUUID(), email, hashedPassword, roles);
    }
}