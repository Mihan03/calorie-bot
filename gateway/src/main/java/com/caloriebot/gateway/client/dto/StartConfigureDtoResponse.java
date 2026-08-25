package com.caloriebot.gateway.client.dto;

public record StartConfigureDtoResponse(
        String userState,
        boolean applied
) {}
