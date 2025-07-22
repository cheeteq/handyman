package com.jakubcitko.handyman.adapters.inbound.security;

import com.jakubcitko.handyman.core.application.port.out.LoadUserAccountPort;
import com.jakubcitko.handyman.core.domain.model.Role;
import com.jakubcitko.handyman.core.domain.model.UserAccount;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    @Mock
    private LoadUserAccountPort loadUserAccountPort;
    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Test
    void should_loadUserAndCreateUserDetails_when_userExists() {
        // GIVEN
        String userEmail = "test@example.com";
        UserAccount mockAccount = new UserAccount(
                UUID.randomUUID(),
                userEmail,
                "hashedPassword",
                Set.of(Role.ROLE_CUSTOMER)
        );
        when(loadUserAccountPort.loadUserByEmail(userEmail)).thenReturn(Optional.of(mockAccount));

        // WHEN
        UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

        // THEN
        assertNotNull(userDetails);
        assertEquals(userEmail, userDetails.getUsername());
        assertEquals("hashedPassword", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_CUSTOMER")));
    }

    @Test
    void should_throwUsernameNotFoundException_when_userDoesNotExist() {
        // GIVEN
        String nonExistentEmail = "ghost@example.com";
        when(loadUserAccountPort.loadUserByEmail(nonExistentEmail)).thenReturn(Optional.empty());

        // WHEN & THEN
        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername(nonExistentEmail);
        });
    }
}