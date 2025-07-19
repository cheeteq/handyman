package com.jakubcitko.handyman.adapters.outband.persistance;

import com.jakubcitko.handyman.core.application.port.out.LoadUserAccountPort;
import com.jakubcitko.handyman.core.domain.model.Role;
import com.jakubcitko.handyman.core.domain.model.UserAccount;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Component
@Profile({"dev", "test"})
public class MockUserAccountPersistenceAdapter implements LoadUserAccountPort {
    @Override
    public Optional<UserAccount> loadUserByEmail(String email) {
        return Optional.of(switch (email) {
            case "user1@mail.com" -> new UserAccount(
                    UUID.fromString("11111111-1111-1111-1111-111111111111"),
                    "user1@mail.com",
                    "hashedPassword1",
                    Set.of(Role.ROLE_CUSTOMER)
            );
            case "greg@mail.com" -> new UserAccount(
                    UUID.fromString("22222222-2222-2222-2222-222222222222"),
                    "greg@mail.com",
                    "hashedPassword2",
                    Set.of(Role.ROLE_HANDYMAN)
            );
            default -> new UserAccount(
                    UUID.randomUUID(),
                    "test@mail.com",
                    "hashedPassword1",
                    Set.of(Role.ROLE_CUSTOMER)
            );
        });
    }
}
