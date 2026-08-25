package com.caloriebot.gateway.service;

import com.caloriebot.gateway.enums.BotKeyboard;
import com.caloriebot.gateway.enums.BotMessage;
import com.caloriebot.gateway.enums.UserState;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Карта состояний бота, см. Readme 15.12
 */
public final class StateMap {
    private static final Map<UserState, Screen> statesMap = Map.of(
            UserState.NEW,  new Screen(BotMessage.WELCOME_MESSAGE, List.of(BotKeyboard.ONB_START)),
            UserState.WAITING_WEIGHT, new Screen(BotMessage.WAITING_WEIGHT, new ArrayList<>()),
            UserState.WAITING_HEIGHT, new Screen(BotMessage.WAITING_HEIGHT, new ArrayList<>())
    );

    public static Screen getScreen(UserState userState) {
        return statesMap.get(userState);
    }
}
