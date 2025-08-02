package com.jakubcitko.handyman.adapters.inbound.web;

import com.jakubcitko.handyman.adapters.inbound.security.AuthService;
import com.jakubcitko.handyman.adapters.inbound.web.dto.*;
import com.jakubcitko.handyman.core.application.port.in.AddAddressToCustomerUseCase;
import com.jakubcitko.handyman.core.application.port.in.CreateServiceRequestUseCase;
import com.jakubcitko.handyman.core.application.port.in.GetCustomerAddressesUseCase;
import com.jakubcitko.handyman.core.application.port.in.GetCustomerRequestsUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final AuthService authService;
    private final AddAddressToCustomerUseCase addAddressUseCase;
    private final GetCustomerAddressesUseCase getCustomerAddressesUseCase;
    private final CreateServiceRequestUseCase createServiceRequestUseCase;
    private final GetCustomerRequestsUseCase getCustomerRequestsUseCase;

    public CustomerController(AuthService authService, AddAddressToCustomerUseCase addAddressUseCase, GetCustomerAddressesUseCase getCustomerAddressesUseCase, CreateServiceRequestUseCase createServiceRequestUseCase, GetCustomerRequestsUseCase getCustomerRequestsUseCase) {
        this.authService = authService;
        this.addAddressUseCase = addAddressUseCase;
        this.getCustomerAddressesUseCase = getCustomerAddressesUseCase;
        this.createServiceRequestUseCase = createServiceRequestUseCase;
        this.getCustomerRequestsUseCase = getCustomerRequestsUseCase;
    }

    @PostMapping("/me/addresses")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<SuccessResponseDto> addAddress(@RequestBody AddAddressRequestDto addressDto) {
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
        return ResponseEntity.ok(new SuccessResponseDto("Address added successfully"));
    }

    @GetMapping("/me/addresses")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<List<AddressResponseDto>> getMyAddresses() {
        var query = new GetCustomerAddressesUseCase.GetCustomerAddressesQuery(authService.getLoggedUserId());
        return ResponseEntity.ok(getCustomerAddressesUseCase.getAddressesForCustomer(query));
    }

    @PostMapping("/me/requests")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<CreateResourceResponseDto> createServiceRequest(@RequestBody CreateServiceRequestDto requestDto) {
        UUID customerId = authService.getLoggedUserId();
        var command = new CreateServiceRequestUseCase.CreateServiceRequestCommand(
                requestDto.title(),
                requestDto.description(),
                customerId,
                requestDto.addressId(),
                requestDto.attachmentIds()
        );

        UUID newRequestId = createServiceRequestUseCase.createServiceRequest(command);
        var response = new CreateResourceResponseDto(newRequestId);

        return ResponseEntity
                .created(URI.create("/api/customers/me/requests/" + newRequestId))
                .body(response);
    }


    @GetMapping("/me/requests")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<List<CustomerRequestSummaryDto>> getServiceRequestsForCustomer() {
        var query = new GetCustomerRequestsUseCase.GetCustomerRequestsQuery(authService.getLoggedUserId());
        return ResponseEntity.ok(getCustomerRequestsUseCase.getRequestsForCustomer(query));
    }


}
