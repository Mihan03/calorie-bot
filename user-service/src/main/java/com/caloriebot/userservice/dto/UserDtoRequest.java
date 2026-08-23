package com.caloriebot.userservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record UserDtoRequest(
        @NotNull
        @Min(0)
        Long tgId
) {}
