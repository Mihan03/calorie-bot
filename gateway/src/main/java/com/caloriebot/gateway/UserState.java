package com.caloriebot.gateway;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserState {
    NEW,
    WAITING_WEIGHT,
    WAITING_HEIGHT
}
