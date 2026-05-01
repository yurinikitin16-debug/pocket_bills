package com.library.pocket.bills.pocket_bills.feature.reading.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record MeterReadingSubmitItemRequest(
        @NotNull(message = "Meter ID is required")
        Long meterId,

        @DecimalMin(value = "0.0", message = "Value cannot be negative")
        BigDecimal value,

        @DecimalMin(value = "0.0", inclusive = false, message = "Consumption must be greater than 0")
        BigDecimal consumption
) {}
