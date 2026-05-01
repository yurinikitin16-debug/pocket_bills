package com.library.pocket.bills.pocket_bills.feature.meter.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record MeterTariffCreateRequest (
        @NotNull(message = "Rate is required")
        @DecimalMin(value = "0.0", inclusive = false, message = "Rate must be greater than 0")
        BigDecimal rate,

        LocalDate establishedDate
) {}
