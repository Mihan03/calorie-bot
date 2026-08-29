package com.caloriebot.userservice.dto;

import com.caloriebot.userservice.model.enums.UserState;

public record RestartResponseDto(
        UserState userState,
        boolean applied
) {
}
