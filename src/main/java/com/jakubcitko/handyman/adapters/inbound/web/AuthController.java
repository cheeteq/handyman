package com.jakubcitko.handyman.adapters.inbound.web;


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
    private final LoadUserAccountPort loadUserAccountPort;

    @Autowired
    public AuthController(
            AuthenticationManager authenticationManager,
            TokenManagerPort tokenManager,
            RegisterCustomerUseCase registerCustomerUseCase,
            LoadUserAccountPort loadUserAccountPort
    ) {
        this.authenticationManager = authenticationManager;
        this.tokenManager = tokenManager;
        this.registerCustomerUseCase = registerCustomerUseCase;
        this.loadUserAccountPort = loadUserAccountPort;
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequestDto loginRequest) {
        // 1. Uwierzytelnij użytkownika.
        // Jeśli dane są błędne, ten krok rzuci wyjątek, który zostanie obsłużony globalnie.
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password())
        );

        // 2. Ustaw uwierzytelnienie w kontekście bezpieczeństwa.
        SecurityContextHolder.getContext().setAuthentication(authentication);


        // 3. Pobierz email z obiektu uwierzytelnienia.
        String email = authentication.getName();

        // 4. Użyj naszego portu, aby pobrać CZYSTY obiekt domenowy UserAccount.
        // W tym momencie mamy pewność, że użytkownik istnieje, bo logowanie się udało.
        UserAccount userAccount = loadUserAccountPort.loadUserByEmail(email)
                .orElseThrow(() -> new IllegalStateException("Authenticated user not found in database: " + email));

        // 5. Wygeneruj token JWT, używając danych z obiektu domenowego.
        String jwt = tokenManager.generateToken(
                userAccount.id(),
                userAccount.email(),
                userAccount.roles()
        );

        // 6. Zbuduj i zwróć odpowiedź.
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