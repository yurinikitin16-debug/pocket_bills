package com.library.pocket.bills.pocket_bills.feature.reading.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record MeterReadingUpdateRequest(

        @DecimalMin(value = "0.0", message = "Value cannot be negative")
        BigDecimal value,

        @DecimalMin(value = "0.0", message = "Value cannot be negative")
        BigDecimal consumption,

        @NotNull(message = "Period month is required")
        @Min(value = 1, message = "Period month must be between 1 and 12")
        @Max(value = 12, message = "Period month must be between 1 and 12")
        Integer periodMonth,

        @NotNull(message = "Period year is required")
        @Min(value = 2000, message = "Period year must be 2000 or later")
        Integer periodYear
) {}
