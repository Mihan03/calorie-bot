package com.caloriebot.userservice.model.enums;

import java.util.List;

public enum UserState {
    NEW,
    WAITING_WEIGHT,
    WAITING_HEIGHT,
    WAITING_GENDER,
    WAITING_ACTIVITY,
    WAITING_GOAL_DIRECTION,
    WAITING_GOAL_CONFIRMATION,
    WAITING_GOAL_EDIT_FIELD;

    // List of onboarding-states
    public static List<UserState> getOnboardingStates() {
        return List.of(WAITING_WEIGHT, WAITING_HEIGHT, WAITING_GENDER, WAITING_ACTIVITY,
                WAITING_GOAL_DIRECTION, WAITING_GOAL_EDIT_FIELD, WAITING_GOAL_CONFIRMATION,
                WAITING_GOAL_EDIT_FIELD);
    }
}
