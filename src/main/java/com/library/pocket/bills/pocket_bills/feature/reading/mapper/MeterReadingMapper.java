package com.library.pocket.bills.pocket_bills.feature.reading.mapper;

import com.library.pocket.bills.pocket_bills.feature.reading.dto.response.MeterReadingResponse;
import com.library.pocket.bills.pocket_bills.feature.reading.entity.MeterReading;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MeterReadingMapper {

    @Mapping(target = "meterId", source = "meter.id")
    @Mapping(target = "serviceId", source = "meter.service.id")
    @Mapping(target = "serviceName", source = "meter.service.name")
    @Mapping(target = "serviceUnit", source = "meter.service.unit")
    MeterReadingResponse toResponse(MeterReading meterReading);
}
