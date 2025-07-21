package com.jakubcitko.handyman.core.application.port.out;

import com.jakubcitko.handyman.core.domain.model.UserAccount;

import java.util.Optional;

public interface LoadUserAccountPort {
    Optional<UserAccount> loadUserByEmail(String email);
}
