package com.caloriebot.gateway.client.dto;

public record StartConfigureResponseDto(
        String userState,
        boolean applied
) {}
