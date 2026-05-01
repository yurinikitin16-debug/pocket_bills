package com.library.pocket.bills.pocket_bills.feature.user.dto.response;

public record UserProfileResponse(
        Long id,
        String email,
        String fullName,
        String phone,
        String role,
        Boolean enabled,
        String lang
) {}
