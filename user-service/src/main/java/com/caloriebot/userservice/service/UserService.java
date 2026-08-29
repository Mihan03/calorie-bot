package com.caloriebot.userservice.service;

import com.caloriebot.userservice.dto.RestartResponseDto;
import com.caloriebot.userservice.dto.StartConfigureResponseDto;
import com.caloriebot.userservice.dto.UserDtoRequest;
import com.caloriebot.userservice.dto.UserDtoResponse;

public interface UserService {
    UserDtoResponse getUserByTgId(Long tgId);
    UserDtoResponse processingStart(UserDtoRequest userDtoRequest);
    StartConfigureResponseDto processingStateStartConfigure(Long tgId);
    RestartResponseDto restartOnboarding(Long tgId);
}