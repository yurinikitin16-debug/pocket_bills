package com.library.pocket.bills.pocket_bills.feature.user.mapper;

import com.library.pocket.bills.pocket_bills.feature.user.dto.response.UserProfileResponse;
import com.library.pocket.bills.pocket_bills.feature.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "role", expression = "java(user.getRole().name())")
    UserProfileResponse toProfileResponse(User user);
}