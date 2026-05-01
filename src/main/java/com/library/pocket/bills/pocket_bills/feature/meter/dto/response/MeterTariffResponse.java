package com.library.pocket.bills.pocket_bills.feature.meter.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public record MeterTariffResponse(
        Long id,
        Long meterId,
        Long serviceId,
        String serviceName,
        String serviceUnit,
        BigDecimal rate,
        LocalDate establishedDate,
        LocalDate endDate,
        Boolean active
) {}