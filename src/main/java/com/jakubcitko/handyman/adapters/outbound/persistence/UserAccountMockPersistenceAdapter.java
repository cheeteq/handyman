package com.jakubcitko.handyman.adapters.outbound.persistence;

import com.jakubcitko.handyman.core.application.port.out.LoadUserAccountPort;
import com.jakubcitko.handyman.core.application.port.out.UserRepositoryPort;
import com.jakubcitko.handyman.core.domain.model.User;
import com.jakubcitko.handyman.core.domain.model.UserAccount;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Profile({"mock"})
public class UserAccountMockPersistenceAdapter implements LoadUserAccountPort {
    private final UserRepositoryPort userRepository;

    public UserAccountMockPersistenceAdapter(UserRepositoryPort userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<UserAccount> loadUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow();
        return Optional.of(new UserAccount(user.getId(), user.getEmail(), user.getPasswordHash(), user.getRoles()));
    }
}
