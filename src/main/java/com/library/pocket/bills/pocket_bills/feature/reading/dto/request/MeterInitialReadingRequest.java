package com.library.pocket.bills.pocket_bills.feature.reading.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record MeterInitialReadingRequest(
        @NotNull
        @DecimalMin(value = "0.0")
        BigDecimal value,

        @NotNull
        @Min(1)
        @Max(12)
        Integer periodMonth,

        @NotNull
        @Min(2000)
        Integer periodYear
) {}