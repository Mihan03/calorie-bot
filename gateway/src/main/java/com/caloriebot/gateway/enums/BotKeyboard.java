package com.caloriebot.gateway.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum BotKeyboard {
    ONB_START("Начать настройку", "onb:start");

    private final String text;
    private final String callback;
}
