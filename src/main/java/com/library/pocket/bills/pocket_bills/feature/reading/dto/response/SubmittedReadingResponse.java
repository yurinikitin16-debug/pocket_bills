package com.library.pocket.bills.pocket_bills.feature.reading.dto.response;

import java.math.BigDecimal;

public record SubmittedReadingResponse(
        Long id,
        BigDecimal value,
        BigDecimal consumption
) {}
