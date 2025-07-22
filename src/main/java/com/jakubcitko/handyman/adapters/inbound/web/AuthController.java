package com.jakubcitko.handyman.adapters.inbound.web;


import com.jakubcitko.handyman.adapters.inbound.security.AuthService;
import com.jakubcitko.handyman.adapters.inbound.web.dto.JwtResponseDto;
import com.jakubcitko.handyman.adapters.inbound.web.dto.LoginRequestDto;
import com.jakubcitko.handyman.adapters.inbound.web.dto.RegisterRequestDto;
import com.jakubcitko.handyman.core.application.port.in.RegisterCustomerUseCase;
import com.jakubcitko.handyman.core.application.port.out.LoadUserAccountPort;
import com.jakubcitko.handyman.core.application.port.out.TokenManagerPort;
import com.jakubcitko.handyman.core.domain.model.Role;
import com.jakubcitko.handyman.core.domain.model.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final TokenManagerPort tokenManager;
    private final RegisterCustomerUseCase registerCustomerUseCase;
    private final AuthService authService;

    @Autowired
    public AuthController(
            AuthenticationManager authenticationManager,
            TokenManagerPort tokenManager,
            RegisterCustomerUseCase registerCustomerUseCase,
            AuthService authService
    ) {
        this.authenticationManager = authenticationManager;
        this.tokenManager = tokenManager;
        this.registerCustomerUseCase = registerCustomerUseCase;
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequestDto loginRequest) {
        UserAccount userAccount = authService.getLoggedUserAccount();

        String jwt = tokenManager.generateToken(
                userAccount.id(),
                userAccount.email(),
                userAccount.roles()
        );

        List<String> roleNames = userAccount.roles().stream().map(Role::name).collect(Collectors.toList());
        return ResponseEntity.ok(new JwtResponseDto(jwt, userAccount.id(), userAccount.email(), roleNames));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequestDto registerRequest) {
        var command = new RegisterCustomerUseCase.RegisterCustomerCommand(
                registerRequest.displayName(),
                registerRequest.phoneNumber(),
                registerRequest.email(),
                registerRequest.password()
        );

        registerCustomerUseCase.registerCustomer(command);

        return ResponseEntity.ok("User registered successfully!");
    }
}