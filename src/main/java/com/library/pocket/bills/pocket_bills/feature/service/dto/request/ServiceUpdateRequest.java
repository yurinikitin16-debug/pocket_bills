package com.library.pocket.bills.pocket_bills.feature.service.dto.request;

import com.library.pocket.bills.pocket_bills.feature.service.entity.BillingType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ServiceUpdateRequest(
        @NotBlank(message = "Service code is required")
        @Size(max = 50, message = "Service code is too long")
        String code,

        @NotBlank(message = "Service name is required")
        @Size(max = 100, message = "Service name is too long")
        String name,

        @Size(max = 30, message = "Service unit is too long")
        String unit,

        @NotNull(message = "Billing type is required")
        BillingType billingType
) {}
