package com.library.pocket.bills.pocket_bills.feature.meter.dto.response;

public record MeterResponse(
        Long id,
        Long serviceId,
        String serviceName,
        String serviceUnit,
        Long defaultTariff,
        Boolean isActive
) {}
