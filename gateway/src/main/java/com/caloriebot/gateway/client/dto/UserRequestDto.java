package com.caloriebot.gateway.client.dto;

import org.jetbrains.annotations.NotNull;

public record UserRequestDto(
      @NotNull
      Long tgId
) {
}
