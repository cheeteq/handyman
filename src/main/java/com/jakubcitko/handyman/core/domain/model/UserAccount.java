package com.jakubcitko.handyman.core.domain.model;

import java.util.Set;
import java.util.UUID;

public record UserAccount(
        UUID id,
        String email,
        String passwordHash,
        Set<Role> roles
) {
}
