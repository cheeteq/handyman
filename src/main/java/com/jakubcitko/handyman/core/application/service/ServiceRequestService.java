package com.jakubcitko.handyman.core.application.service;

import com.jakubcitko.handyman.core.application.port.in.CreateServiceRequestUseCase;
import com.jakubcitko.handyman.core.application.port.out.ServiceRequestRepositoryPort;
import com.jakubcitko.handyman.core.domain.model.ServiceRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ServiceRequestService implements CreateServiceRequestUseCase {

    private final ServiceRequestRepositoryPort serviceRequestRepository;

    public ServiceRequestService(ServiceRequestRepositoryPort serviceRequestRepository) {
        this.serviceRequestRepository = serviceRequestRepository;
    }

    @Override
    public void createServiceRequest(CreateServiceRequestCommand command) {
        ServiceRequest request = ServiceRequest.createNew(
                command.title(),
                command.description(),
                command.customerId(),
                command.addressId(),
                command.attachments()
        );

        serviceRequestRepository.save(request);
    }
}
