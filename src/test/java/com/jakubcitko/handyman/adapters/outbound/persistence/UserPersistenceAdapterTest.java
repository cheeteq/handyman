package com.jakubcitko.handyman.adapters.outbound.persistence;

import com.jakubcitko.handyman.AbstractSpringBootTest;
import com.jakubcitko.handyman.core.domain.model.Role;
import com.jakubcitko.handyman.core.domain.model.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserPersistenceAdapterTest extends AbstractSpringBootTest {
    @Autowired
    UserPersistenceAdapter persistenceAdapter;

    @Test
    void test_saveUser() {
        //GIVEN
        String email = "test@mail.com";
        String password = "hashedPassword";
        Set<Role> roles = Set.of(Role.ROLE_CUSTOMER);
        User user = User.register(email, password, roles);
        UUID id = user.getId();

        //WHEN
        persistenceAdapter.save(user);

        //THEN
        assertTrue(persistenceAdapter.existsByEmail(email));
        User savedUser = persistenceAdapter.findById(id).orElseThrow();
        assertEquals(email, savedUser.getEmail());
        assertEquals(password, savedUser.getPasswordHash());
        Assertions.assertThat(savedUser.getRoles())
                .containsExactlyElementsOf(roles);
    }

}