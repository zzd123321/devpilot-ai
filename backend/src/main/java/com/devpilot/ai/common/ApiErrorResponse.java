package com.devpilot.ai.common;

import java.time.Instant;
import java.util.List;

public record ApiErrorResponse(
        String code,
        String message,
        Instant timestamp,
        List<FieldErrorResponse> fieldErrors
) {
}
