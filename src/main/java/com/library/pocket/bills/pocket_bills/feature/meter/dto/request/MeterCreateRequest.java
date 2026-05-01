package com.library.pocket.bills.pocket_bills.feature.meter.dto.request;

import com.library.pocket.bills.pocket_bills.feature.reading.dto.request.MeterInitialReadingRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record MeterCreateRequest(
        @NotNull(message = "Service ID is required")
        Long serviceId,

        @Valid
        @NotNull(message = "Initial tariff is required")
        MeterTariffCreateRequest initialTariff,

        @Valid
        MeterInitialReadingRequest initialReading
) {}