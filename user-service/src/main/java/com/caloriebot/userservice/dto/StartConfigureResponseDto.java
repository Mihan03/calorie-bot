package com.caloriebot.userservice.dto;

import com.caloriebot.userservice.model.enums.UserState;

public record StartConfigureResponseDto(
        UserState userState,
        boolean applied
) {}
