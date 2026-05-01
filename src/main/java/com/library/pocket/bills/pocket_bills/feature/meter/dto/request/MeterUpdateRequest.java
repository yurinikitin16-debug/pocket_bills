package com.library.pocket.bills.pocket_bills.feature.meter.dto.request;

import jakarta.validation.constraints.NotNull;

public record MeterUpdateRequest(
        @NotNull(message = "Service ID is required")
        Long serviceId,

        @NotNull(message = "Active status is required")
        Boolean isActive
) {
}

