package com.library.pocket.bills.pocket_bills.feature.reading.dto.response;

import com.library.pocket.bills.pocket_bills.feature.service.entity.BillingType;

import java.math.BigDecimal;

public record MeterReadingFormItemResponse(
        Long meterId,
        Long serviceId,
        String serviceName,
        String serviceUnit,
        BillingType billingType,
        BigDecimal previousValue,
        BigDecimal currentTariffRate,
        Boolean alreadySubmitted,
        SubmittedReadingResponse submittedReading
) {}