package com.caloriebot.userservice.service;

import com.caloriebot.userservice.dto.RestartResponseDto;
import com.caloriebot.userservice.dto.StartConfigureResponseDto;
import com.caloriebot.userservice.dto.UserDtoRequest;
import com.caloriebot.userservice.dto.UserDtoResponse;

/**
 *
 * @author Mikhail Feoktistov
 */
public interface OnboardingService {
    UserDtoResponse processingStart(UserDtoRequest userDtoRequest);
    StartConfigureResponseDto processingStateStartConfigure(Long tgId);
    RestartResponseDto restartOnboarding(Long tgId);
}
