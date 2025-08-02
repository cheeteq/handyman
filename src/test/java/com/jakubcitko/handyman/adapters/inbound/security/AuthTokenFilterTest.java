package com.jakubcitko.handyman.adapters.inbound.security;

import com.jakubcitko.handyman.AbstractSpringBootTest;
import com.jakubcitko.handyman.core.application.port.out.TokenManagerPort;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.io.IOException;
import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuthTokenFilterTest extends AbstractSpringBootTest {
    @Autowired
    private AuthTokenFilter authTokenFilter;
    @Autowired
    private TokenManagerPort tokenManager;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public UserDetailsService userDetailsService() {
            UserDetailsService mock = mock(UserDetailsService.class);
            UserDetails mockUserDetails = new User("test@example.com", "password", Collections.emptyList());
            when(mock.loadUserByUsername(anyString())).thenReturn(mockUserDetails);
            return mock;
        }
    }

    @Test
    void should_setAuthenticationInContext_when_tokenIsValid() throws ServletException, IOException {
        // GIVEN
        String validToken = tokenManager.generateToken(UUID.randomUUID(), "test@example.com", Collections.emptySet());
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + validToken);

        // WHEN
        authTokenFilter.doFilterInternal(request, new MockHttpServletResponse(), mock(FilterChain.class));

        // THEN
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertTrue(SecurityContextHolder.getContext().getAuthentication().isAuthenticated());

        SecurityContextHolder.clearContext();
    }
}