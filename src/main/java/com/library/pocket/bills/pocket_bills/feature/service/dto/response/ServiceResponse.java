package com.library.pocket.bills.pocket_bills.feature.service.dto.response;

import com.library.pocket.bills.pocket_bills.feature.service.entity.BillingType;

public record ServiceResponse(
        Long id,
        String code,
        String name,
        String unit,
        BillingType billingType
) {}
