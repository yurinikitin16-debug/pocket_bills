package com.library.pocket.bills.pocket_bills.feature.meter.mapper;

import com.library.pocket.bills.pocket_bills.feature.meter.dto.response.MeterTariffResponse;
import com.library.pocket.bills.pocket_bills.feature.meter.entity.MeterTariff;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MeterTariffMapper {

    @Mapping(target = "meterId", source = "meter.id")
    @Mapping(target = "serviceId", source = "meter.service.id")
    @Mapping(target = "serviceName", source = "meter.service.name")
    @Mapping(target = "serviceUnit", source = "meter.service.unit")
    @Mapping(target = "active", expression = "java(meterTariff.getEndDate() == null)")
    MeterTariffResponse toResponse(MeterTariff meterTariff);
}
