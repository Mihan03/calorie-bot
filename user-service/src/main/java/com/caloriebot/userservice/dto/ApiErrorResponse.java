package com.caloriebot.userservice.dto;

import java.util.List;

public record ApiErrorResponse(
        String correlationId,
        List<ApiError> errors
) {
}
