package com.jakubcitko.handyman.adapters.inbound.web;

import com.jakubcitko.handyman.adapters.inbound.security.AuthService;
import com.jakubcitko.handyman.adapters.inbound.web.dto.AddAddressRequestDto;
import com.jakubcitko.handyman.adapters.inbound.web.dto.AddressResponseDto;
import com.jakubcitko.handyman.core.application.port.in.AddAddressToCustomerUseCase;
import com.jakubcitko.handyman.core.application.port.in.GetCustomerAddressesUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers") // Zasobem, którym zarządzamy, jest 'customer'
public class CustomerController {

    private final AddAddressToCustomerUseCase addAddressUseCase;
    private final GetCustomerAddressesUseCase getCustomerAddressesUseCase;
    private final AuthService authService;

    public CustomerController(AddAddressToCustomerUseCase addAddressUseCase, GetCustomerAddressesUseCase getCustomerAddressesUseCase, AuthService authService) {
        this.addAddressUseCase = addAddressUseCase;
        this.getCustomerAddressesUseCase = getCustomerAddressesUseCase;
        this.authService = authService;
    }

    @PostMapping("/me/addresses")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<String> addAddress(@RequestBody AddAddressRequestDto addressDto) {
        var addressData = new AddAddressToCustomerUseCase.AddressData(
                addressDto.street(),
                addressDto.streetNumber(),
                addressDto.flatNumber(),
                addressDto.city(),
                addressDto.postalCode()
        );
        var command = new AddAddressToCustomerUseCase.AddAddressToCustomerCommand(
                authService.getLoggedUserId(),
                addressData
        );
        addAddressUseCase.addAddressToCustomer(command);
        return ResponseEntity.ok("Address added successfully.");
    }

    @GetMapping("/me/addresses")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<List<AddressResponseDto>> getMyAddresses() {
        var query = new GetCustomerAddressesUseCase.GetCustomerAddressesQuery(authService.getLoggedUserId());
        return ResponseEntity.ok(getCustomerAddressesUseCase.getAddressesForCustomer(query));
    }


}
