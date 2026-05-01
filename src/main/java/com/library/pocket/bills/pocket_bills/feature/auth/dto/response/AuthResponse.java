package com.library.pocket.bills.pocket_bills.feature.auth.dto.response;

import com.library.pocket.bills.pocket_bills.feature.user.dto.response.UserProfileResponse;

public record AuthResponse(
        String token,
        String tokenType,
        Long expiresIn,           // в секундах
        UserProfileResponse user
) {}
