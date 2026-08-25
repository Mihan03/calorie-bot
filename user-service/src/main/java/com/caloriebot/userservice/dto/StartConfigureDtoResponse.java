package com.caloriebot.userservice.dto;

import com.caloriebot.userservice.model.enums.UserState;

public record StartConfigureDtoResponse(
        UserState userState,
        boolean applied
) {}
