package com.jakubcitko.handyman.adapters.inbound.web;

import com.jakubcitko.handyman.adapters.inbound.web.dto.JwtResponseDto;
import com.jakubcitko.handyman.adapters.inbound.web.dto.LoginRequestDto;
import com.jakubcitko.handyman.adapters.inbound.web.dto.RegisterRequestDto;
import com.jakubcitko.handyman.core.application.port.in.RegisterCustomerUseCase;
import com.jakubcitko.handyman.core.application.port.out.LoadUserAccountPort;
import com.jakubcitko.handyman.core.application.port.out.TokenManagerPort;
import com.jakubcitko.handyman.core.domain.model.Role;
import com.jakubcitko.handyman.core.domain.model.UserAccount;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {
    @Mock
    AuthenticationManager authenticationManager;
    @Mock
    TokenManagerPort tokenManager;
    @Mock
    RegisterCustomerUseCase registerCustomerUseCase;
    @Mock
    LoadUserAccountPort loadUserAccountPort;
    @InjectMocks
    AuthController authController;

    @Test
    void should_returnJwtResponse_when_loginIsSuccessful() {
        // GIVEN
        String email = "test@example.com";
        String password = "password";
        LoginRequestDto loginRequest = new LoginRequestDto(email, password);
        Authentication mockAuthentication = new UsernamePasswordAuthenticationToken(email, password);
        UserAccount mockAccount = new UserAccount(
                UUID.randomUUID(),
                email,
                "hashedPassword",
                Set.of(Role.ROLE_CUSTOMER)
        );
        String mockJwt = "mockJwtToken";

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mockAuthentication);
        when(loadUserAccountPort.loadUserByEmail(email)).thenReturn(Optional.of(mockAccount));
        when(tokenManager.generateToken(mockAccount.id(), mockAccount.email(), mockAccount.roles()))
                .thenReturn(mockJwt);

        // WHEN
        ResponseEntity<?> response = authController.loginUser(loginRequest);

        // THEN
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        JwtResponseDto jwtResponse = (JwtResponseDto) response.getBody();
        assertNotNull(jwtResponse);
        assertEquals(mockJwt, jwtResponse.accessToken());
        assertEquals(mockAccount.id(), jwtResponse.id());
        assertEquals(mockAccount.email(), jwtResponse.email());
        assertTrue(jwtResponse.roles().contains("ROLE_CUSTOMER"));
    }

    @Test
    void should_throwException_when_authenticatedUserNotFoundInDatabase() {
        // GIVEN
        String email = "ghost@example.com";
        String password = "password";
        LoginRequestDto loginRequest = new LoginRequestDto(email, password);
        Authentication mockAuthentication = new UsernamePasswordAuthenticationToken(email, password);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mockAuthentication);
        when(loadUserAccountPort.loadUserByEmail(email)).thenReturn(Optional.empty());

        // WHEN & THEN
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            authController.loginUser(loginRequest);
        });
        assertEquals("Authenticated user not found in database: " + email, exception.getMessage());
    }

    @Test
    void should_registerUserSuccessfully_when_validRequestProvided() {
        // GIVEN
        RegisterRequestDto registerRequest = new RegisterRequestDto(
                "Test User",
                "123456789",
                "test@example.com",
                "password"
        );

        // WHEN
        ResponseEntity<?> response = authController.registerUser(registerRequest);

        // THEN
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("User registered successfully!", response.getBody());
        verify(registerCustomerUseCase, times(1)).registerCustomer(any());
    }

}