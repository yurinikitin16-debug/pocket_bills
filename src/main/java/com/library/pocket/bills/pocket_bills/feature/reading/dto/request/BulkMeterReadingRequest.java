package com.library.pocket.bills.pocket_bills.feature.reading.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record BulkMeterReadingRequest(
        @NotNull(message = "Period month is required")
        @Min(value = 1, message = "Period month must be between 1 and 12")
        @Max(value = 12, message = "Period month must be between 1 and 12")
        Integer periodMonth,

        @NotNull(message = "Period year is required")
        @Min(value = 2000, message = "Period year must be 2000 or later")
        Integer periodYear,

        @Valid
        @NotEmpty(message = "Readings are required")
        List<MeterReadingSubmitItemRequest> readings
) {}
