package com.library.pocket.bills.pocket_bills.feature.bill.dto.response;

import com.library.pocket.bills.pocket_bills.feature.service.entity.BillingType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record BillResponse(
        Long id,
        Long meterReadingId,
        Long meterId,
        Long serviceId,
        String serviceName,
        String serviceUnit,
        BillingType billingType,
        BigDecimal previousValue,
        BigDecimal currentValue,
        BigDecimal consumption,
        BigDecimal tariffRate,
        BigDecimal amount,
        Integer periodMonth,
        Integer periodYear,
        LocalDateTime calculatedAt
) {}
