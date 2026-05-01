package com.library.pocket.bills.pocket_bills.feature.reading.dto.response;

import java.util.List;

public record ReadingFormDataResponse(
        Integer periodMonth,
        Integer periodYear,
        List<MeterReadingFormItemResponse> meters
) {}
