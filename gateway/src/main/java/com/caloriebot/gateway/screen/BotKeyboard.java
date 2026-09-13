package com.caloriebot.gateway.screen;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum BotKeyboard {
    ONB_START("Начать настройку", "onb:start"),
    ONB_RESTART("Начать заново", "onb:restart");

    private final String text;
    private final String callback;
}
