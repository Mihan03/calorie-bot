package com.caloriebot.userservice.dto;


import jakarta.validation.constraints.NotBlank;

public record UserDto(
        @NotBlank(message = "tgId must be not blank")
        String tgId
) {}
