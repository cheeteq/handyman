package com.jakubcitko.handyman.core.application.service;

import com.jakubcitko.handyman.core.application.port.in.CreateServiceRequestUseCase;
import com.jakubcitko.handyman.core.application.port.out.AttachmentRepositoryPort;
import com.jakubcitko.handyman.core.application.port.out.CustomerRepositoryPort;
import com.jakubcitko.handyman.core.application.port.out.FileStoragePort;
import com.jakubcitko.handyman.core.application.port.out.ServiceRequestRepositoryPort;
import com.jakubcitko.handyman.core.domain.exception.BusinessRuleViolationException;
import com.jakubcitko.handyman.core.domain.model.Attachment;
import com.jakubcitko.handyman.core.domain.model.Customer;
import com.jakubcitko.handyman.core.domain.model.ServiceRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class ServiceRequestService implements CreateServiceRequestUseCase {

    private final ServiceRequestRepositoryPort serviceRequestRepository;
    private final CustomerRepositoryPort customerRepository;
    private final FileStoragePort fileStoragePort;
    private final AttachmentRepositoryPort attachmentRepository;

    public ServiceRequestService(
            ServiceRequestRepositoryPort serviceRequestRepository,
            CustomerRepositoryPort customerRepository,
            FileStoragePort fileStoragePort, AttachmentRepositoryPort attachmentRepository
    ) {
        this.serviceRequestRepository = serviceRequestRepository;
        this.customerRepository = customerRepository;
        this.fileStoragePort = fileStoragePort;
        this.attachmentRepository = attachmentRepository;
    }

    @Override
    public UUID createServiceRequest(CreateServiceRequestCommand command) {
        validateAddressOwnership(command.customerId(), command.addressId());

        List<Attachment> attachmentsToAssign = validateAndGetAttachments(
                command.customerId(),
                command.attachmentsIds()
        );

        ServiceRequest request = ServiceRequest.createNew(
                command.title(),
                command.description(),
                command.customerId(),
                command.addressId()
        );

        serviceRequestRepository.save(request);

        for (Attachment attachment : attachmentsToAssign) {
            Attachment updatedAttachment = attachment.assignToServiceRequest(request.getId());
            attachmentRepository.save(updatedAttachment);
        }

        return request.getId();
    }

    private List<Attachment> validateAndGetAttachments(UUID customerId, List<UUID> attachmentIds) {
        if (attachmentIds == null || attachmentIds.isEmpty()) {
            return Collections.emptyList();
        }
        List<Attachment> foundAttachments = attachmentRepository.findAllByIds(attachmentIds);

        if (foundAttachments.size() != attachmentIds.size()) {
            throw new BusinessRuleViolationException("One or more attachments could not be found.");
        }

        boolean allValid = foundAttachments.stream().allMatch(att ->
                att.uploaderId().equals(customerId) && att.serviceRequestId() == null
        );

        if (!allValid) {
            throw new BusinessRuleViolationException("One or more attachments are invalid...");
        }

        if (!fileStoragePort.doObjectsExist(attachmentIds)) {
            throw new BusinessRuleViolationException("One or more attachments could not be found in the file storage. The upload may have failed.");
        }

        return foundAttachments;
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
}
