package com.library.pocket.bills.pocket_bills.feature.reading.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MeterReadingResponse(
        Long id,
        Long meterId,
        Long serviceId,
        String serviceName,
        String serviceUnit,
        BigDecimal previousValue,
        BigDecimal value,
        BigDecimal consumption,
        Integer periodMonth,
        Integer periodYear,
        LocalDateTime readingDate
) {}
