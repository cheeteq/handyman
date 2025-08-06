package com.jakubcitko.handyman.core.application.service;

import com.jakubcitko.handyman.core.application.port.in.CreateServiceRequestUseCase;
import com.jakubcitko.handyman.core.application.port.out.AttachmentRepositoryPort;
import com.jakubcitko.handyman.core.application.port.out.CustomerRepositoryPort;
import com.jakubcitko.handyman.core.application.port.out.FileStoragePort;
import com.jakubcitko.handyman.core.application.port.out.ServiceRequestRepositoryPort;
import com.jakubcitko.handyman.core.domain.model.Address;
import com.jakubcitko.handyman.core.domain.model.Attachment;
import com.jakubcitko.handyman.core.domain.model.Customer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class ServiceRequestServiceTest {

    @Mock
    private ServiceRequestRepositoryPort serviceRequestRepository;

    @Mock
    private CustomerRepositoryPort customerRepository;

    @Mock
    private FileStoragePort fileStoragePort;

    @Mock
    private AttachmentRepositoryPort attachmentRepository;

    @InjectMocks
    private ServiceRequestService serviceRequestService;

    @Test
    void test_createServiceRequest_withNoAttachments() {
        //GIVEN
        UUID customerId = UUID.randomUUID();
        Customer customer = Customer.registerNew(customerId, "testName", "500600700");
        Address address = Address.createNew("streetName", "5", "5", "city", "XX-XXX");
        customer.addAddress(address);
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

        String title = "test_title";
        String description = "test_description";
        UUID addressId = address.getId();
        CreateServiceRequestUseCase.CreateServiceRequestCommand command = new CreateServiceRequestUseCase
                .CreateServiceRequestCommand(title, description, customerId, addressId, null);

        //WHEN
        serviceRequestService.createServiceRequest(command);

        //THEN
        verify(customerRepository).findById(customerId);
        verify(serviceRequestRepository).save(any());
        verify(attachmentRepository, never()).findAllByIds(any());
        verify(attachmentRepository, never()).save(any());
        verify(fileStoragePort, never()).doObjectsExist(any());
    }

    @Test
    void test_createServiceRequest_withAttachments() {
        //GIVEN
        UUID customerId = UUID.randomUUID();
        Customer customer = Customer.registerNew(customerId, "testName", "500600700");
        Address address = Address.createNew("streetName", "5", "5", "city", "XX-XXX");
        customer.addAddress(address);
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

        Attachment attachment1 = Attachment.createNew("test1", "image/jpeg", 1000, customerId);
        Attachment attachment2 = Attachment.createNew("test2", "image/jpeg", 1000, customerId);
        List<UUID> attachmentIds = List.of(attachment1.id(), attachment2.id());
        when(attachmentRepository.findAllByIds(attachmentIds)).thenReturn(List.of(attachment1, attachment2));
        when(fileStoragePort.doObjectsExist(attachmentIds)).thenReturn(true);

        String title = "test_title";
        String description = "test_description";
        UUID addressId = address.getId();
        CreateServiceRequestUseCase.CreateServiceRequestCommand command = new CreateServiceRequestUseCase
                .CreateServiceRequestCommand(title, description, customerId, addressId, attachmentIds);

        //WHEN
        var serviceId = serviceRequestService.createServiceRequest(command);

        //THEN
        verify(customerRepository).findById(customerId);
        verify(serviceRequestRepository).save(any());
        verify(attachmentRepository).findAllByIds(attachmentIds);
        verify(attachmentRepository, times(2)).save(any());
        verify(fileStoragePort).doObjectsExist(attachmentIds);
    }


}