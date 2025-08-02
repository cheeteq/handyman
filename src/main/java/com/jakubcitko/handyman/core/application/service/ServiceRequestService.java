package com.jakubcitko.handyman.core.application.service;

import com.jakubcitko.handyman.core.application.port.in.CreateServiceRequestUseCase;
import com.jakubcitko.handyman.core.application.port.out.CustomerRepositoryPort;
import com.jakubcitko.handyman.core.application.port.out.FileStoragePort;
import com.jakubcitko.handyman.core.application.port.out.ServiceRequestRepositoryPort;
import com.jakubcitko.handyman.core.domain.exception.BusinessRuleViolationException;
import com.jakubcitko.handyman.core.domain.model.Customer;
import com.jakubcitko.handyman.core.domain.model.ServiceRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@Transactional
public class ServiceRequestService implements CreateServiceRequestUseCase {

    private final ServiceRequestRepositoryPort serviceRequestRepository;
    private final CustomerRepositoryPort customerRepository;
    private final FileStoragePort fileStoragePort;

    public ServiceRequestService(ServiceRequestRepositoryPort serviceRequestRepository, CustomerRepositoryPort customerRepository, FileStoragePort fileStoragePort) {
        this.serviceRequestRepository = serviceRequestRepository;
        this.customerRepository = customerRepository;
        this.fileStoragePort = fileStoragePort;
    }

    @Override
    public UUID createServiceRequest(CreateServiceRequestCommand command) {

        validateAddressOwnership(command.customerId(), command.addressId());
        validateAttachments(command.attachments());

        ServiceRequest request = ServiceRequest.createNew(
                command.title(),
                command.description(),
                command.customerId(),
                command.addressId(),
                command.attachments()
        );

        return serviceRequestRepository.save(request);
    }


    private void validateAddressOwnership(UUID customerId, UUID addressId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new BusinessRuleViolationException("Customer with id: %s not found during validation.", customerId));

        boolean addressBelongsToCustomer = customer.getAddresses().stream()
                .anyMatch(address -> address.getId().equals(addressId));

        if (!addressBelongsToCustomer) {
            throw new BusinessRuleViolationException(
                    "Address with id %s does not belong to customer %s.", addressId, customerId
            );
        }
    }

    private void validateAttachments(List<UUID> attachmentIds) {
        if (Objects.isNull(attachmentIds) || attachmentIds.isEmpty())
            return;
        boolean allAttachmentsExists = fileStoragePort.doObjectsExist(attachmentIds);
        if (!allAttachmentsExists) {
            throw new BusinessRuleViolationException(
                    "Some of attachments not found: %s", attachmentIds.toString()
            );
        }
    }
}
