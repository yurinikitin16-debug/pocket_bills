package com.library.pocket.bills.pocket_bills.feature.reading.dto.response;

import com.library.pocket.bills.pocket_bills.feature.bill.dto.response.BillResponse;

import java.util.List;

public record BulkMeterReadingResponse(
        Integer periodMonth,
        Integer periodYear,
        List<MeterReadingResponse> readings,
        List<BillResponse> bills
) {}
