package com.jakubcitko.handyman.adapters.inbound.security.jwt;

import com.jakubcitko.handyman.core.domain.model.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class JwtTokenManagerAdapterTest {

    private JwtTokenManagerAdapter tokenManager;

    @BeforeEach
    void setUp() {
        tokenManager = new JwtTokenManagerAdapter();
        ReflectionTestUtils.setField(tokenManager, "jwtSecret", "aVeryLongAndSecureSecretKeyForTestsThatIsAtLeast512BitsLong");
        ReflectionTestUtils.setField(tokenManager, "jwtExpirationMs", 3600000); // 1 godzina
    }

    @Test
    void should_generateValidToken_and_retrieveEmailFromIt() {
        // GIVEN
        UUID userId = UUID.randomUUID();
        String email = "test@example.com";
        Set<Role> roles = Set.of(Role.ROLE_CUSTOMER, Role.ROLE_HANDYMAN);

        // WHEN
        String token = tokenManager.generateToken(userId, email, roles);

        // THEN
        assertNotNull(token);
        assertFalse(token.isBlank());

        // Sprawdzamy, czy ten sam token jest walidowany jako poprawny
        assertTrue(tokenManager.isTokenValid(token));

        // Sprawdzamy, czy możemy odczytać z niego poprawne dane
        assertEquals(email, tokenManager.getEmailFromToken(token));

        // Można by też dodać test na odczyt ról, jeśli dodasz taką metodę
        // assertEquals(roles, tokenManager.getRolesFromToken(token));
    }

    @Test
    void should_invalidateTamperedToken() {
        // GIVEN
        String tamperedToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c" +
                "tampered"; // Dopisujemy coś do podpisu

        // WHEN & THEN
        assertFalse(tokenManager.isTokenValid(tamperedToken));
    }
}