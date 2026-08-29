package com.caloriebot.gateway;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserState {
    NEW,
    WAITING_WEIGHT,
    WAITING_HEIGHT,
    WAITING_GENDER,
    WAITING_ACTIVITY,
    WAITING_GOAL_DIRECTION,
    WAITING_GOAL_CONFIRMATION,
    WAITING_GOAL_EDIT_FIELD,
    WAITING_GOAL_EDIT_VALUE
}
