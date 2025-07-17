package com.jakubcitko.handyman.core.application.port.in;

import java.util.List;
import java.util.UUID;

public interface CreateServiceRequestUseCase {
    void createServiceRequest(CreateServiceRequestCommand command);
    record CreateServiceRequestCommand(
            String title,
            String description,
            String customerName,
            String customerPhone,
            String customerEmail,
            AddressData address,
            List<FileReferenceInput> attachments
    ) {}

    record AddressData(
            String street,
            String streetNumber,
            String flatNumber,
            String city,
            String postalCode
    ) {}

    record FileReferenceInput(
            UUID fileUid,
            String originalFilename
    ) {}
}
