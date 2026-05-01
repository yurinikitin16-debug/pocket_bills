package com.library.pocket.bills.pocket_bills.feature.user.dto.request;

import com.library.pocket.bills.pocket_bills.feature.user.entity.Lang;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserUpdateRequest(
        @Size(max = 255, message = "Full name is too long")
        String fullName,

        @Size(max = 50, message = "Phone number is too long")
        String phone,

        @NotNull
        Lang lang
) {}