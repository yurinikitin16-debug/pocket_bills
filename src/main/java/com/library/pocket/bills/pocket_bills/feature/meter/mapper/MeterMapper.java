package com.library.pocket.bills.pocket_bills.feature.meter.mapper;

import com.library.pocket.bills.pocket_bills.feature.meter.dto.response.MeterResponse;
import com.library.pocket.bills.pocket_bills.feature.meter.entity.Meter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MeterMapper {

    @Mapping(target = "serviceId", source = "service.id")
    @Mapping(target = "serviceName", source = "service.name")
    @Mapping(target = "serviceUnit", source = "service.unit")
    MeterResponse toResponse(Meter meter);
}
