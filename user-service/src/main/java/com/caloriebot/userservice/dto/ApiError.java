package com.caloriebot.userservice.dto;

public record ApiError(
        String code,
        String message,
        String userMessage,
        String field
) {
}
