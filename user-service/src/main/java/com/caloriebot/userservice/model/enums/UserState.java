package com.caloriebot.userservice.model.enums;

import java.util.List;

public enum UserState {
    NEW,
    WAITING_WEIGHT,
    WAITING_HEIGHT;

    /**
     * Состояния незавершённого onboarding: из них разрешён переход «Начать заново».
     * Список пополняется вместе с реализацией нового шага, см. README 15.10.
     */
    public static List<UserState> getOnboardingStates() {
        return List.of(WAITING_WEIGHT, WAITING_HEIGHT);
    }
}
