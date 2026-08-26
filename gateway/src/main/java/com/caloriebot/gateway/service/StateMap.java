package com.caloriebot.gateway.service;

import com.caloriebot.gateway.enums.BotKeyboard;
import com.caloriebot.gateway.enums.BotMessage;
import com.caloriebot.gateway.enums.UserState;

import java.util.List;
import java.util.Map;

/**
 * Карта состояний бота, см. Readme 15.12
 */
public final class StateMap {
    private static final Map<UserState, Screen> STATES_MAP = Map.of(
            UserState.NEW,  new Screen(BotMessage.WELCOME_MESSAGE, List.of(BotKeyboard.ONB_START)),
            UserState.WAITING_WEIGHT, new Screen(BotMessage.WAITING_WEIGHT,
                    List.of(BotKeyboard.ONB_RESTART)),
            UserState.WAITING_HEIGHT, new Screen(BotMessage.WAITING_HEIGHT, List.of())
    );

    public static Screen getScreen(UserState userState) {
        return STATES_MAP.get(userState);
    }
}
