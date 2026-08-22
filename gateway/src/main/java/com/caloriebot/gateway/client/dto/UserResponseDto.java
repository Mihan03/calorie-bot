package com.caloriebot.gateway.client.dto;

import java.util.UUID;

public record UserResponseDto(
        UUID userId,
        String userState
) {}