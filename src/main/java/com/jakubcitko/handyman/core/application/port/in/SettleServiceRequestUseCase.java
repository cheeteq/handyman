package com.jakubcitko.handyman.core.application.port.in;

import java.math.BigDecimal;
import java.util.UUID;

public interface SettleServiceRequestUseCase {
    void settleServiceRequest(SettleServiceRequestCommand command);
    record SettleServiceRequestCommand (
            UUID serviceRequestId,
            BigDecimal finalRevenue,
            BigDecimal costsOfParts
    ) {}
}
