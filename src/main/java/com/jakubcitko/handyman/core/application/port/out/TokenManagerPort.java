package com.jakubcitko.handyman.core.application.port.out;

import com.jakubcitko.handyman.core.domain.model.Role;

import java.util.Set;
import java.util.UUID;


public interface TokenManagerPort {

    String generateToken(UUID id, String email, Set<Role> roles);

    String getEmailFromToken(String token);

    boolean isTokenValid(String token);
}
