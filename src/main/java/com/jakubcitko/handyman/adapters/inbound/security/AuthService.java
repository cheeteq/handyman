package com.jakubcitko.handyman.adapters.inbound.security;

import com.jakubcitko.handyman.core.application.port.out.LoadUserAccountPort;
import com.jakubcitko.handyman.core.domain.model.UserAccount;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthService {
    private final LoadUserAccountPort loadUserAccountPort;

    public AuthService(LoadUserAccountPort loadUserAccountPort) {
        this.loadUserAccountPort = loadUserAccountPort;
    }

    public UserAccount getLoggedUserAccount() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        return loadUserAccountPort.loadUserByEmail(userEmail)
                .orElseThrow(() -> new IllegalStateException("Authenticated user not found in database: " + userEmail));
    }
}
