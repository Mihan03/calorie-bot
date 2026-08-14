package com.caloriebot.userservice.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    USER_NOT_FOUND(
            "Сначала зарегистрируйтесь с помощью команды /start"
    );

    private final String userMessage;
}
