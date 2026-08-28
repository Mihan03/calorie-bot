package com.caloriebot.gateway.screen;

import java.util.List;

public record Screen(
        BotMessage message,
        List<BotKeyboard> keyboards
) {}
