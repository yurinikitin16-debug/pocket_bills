package com.library.pocket.bills.pocket_bills.feature.service.mapper;

import com.library.pocket.bills.pocket_bills.feature.service.dto.response.ServiceResponse;
import com.library.pocket.bills.pocket_bills.feature.service.entity.Service;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ServiceMapper {

    ServiceResponse toResponse(Service service);
}