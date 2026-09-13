package com.caloriebot.gateway.client.dto;

public record RestartResponseDto(
        String userState,
        boolean applied
) {
}
