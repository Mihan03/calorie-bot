package com.caloriebot.userservice.service;

import com.caloriebot.userservice.dto.StartConfigureResponseDto;
import com.caloriebot.userservice.dto.UserDtoResponse;
import com.caloriebot.userservice.model.entity.UserEntity;
import com.caloriebot.userservice.model.enums.UserState;

public interface UserService {
    UserDtoResponse getUserByTgId(Long tgId);
    StartConfigureResponseDto changeState(Long tgId, UserState fromState, UserState toState);
    UserEntity getUserEntity(Long tgId);
}