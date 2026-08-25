package com.caloriebot.gateway.service;

import com.caloriebot.gateway.enums.BotKeyboard;
import com.caloriebot.gateway.enums.BotMessage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public final class Screen {
    private final BotMessage message;
    private final List<BotKeyboard> keyboards;
}
