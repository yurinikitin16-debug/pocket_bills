package com.library.pocket.bills.pocket_bills.common.exception;

import java.time.LocalDateTime;
import java.util.Map;

public record ErrorResponse(
        int status,
        String errorCode,
        String message,
        LocalDateTime timestamp,
        Map<String, String> errors   // для валідації
) {
    public ErrorResponse(int status, String errorCode, String message, LocalDateTime timestamp) {
        this(status, errorCode, message, timestamp, null);
    }
}