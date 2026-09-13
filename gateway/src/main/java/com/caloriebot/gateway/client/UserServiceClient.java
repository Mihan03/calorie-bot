package com.caloriebot.gateway.client;

import com.caloriebot.gateway.client.dto.RestartResponseDto;
import com.caloriebot.gateway.client.dto.StartConfigureResponseDto;
import com.caloriebot.gateway.client.dto.UserRequestDto;
import com.caloriebot.gateway.client.dto.UserResponseDto;

public interface UserServiceClient {
   UserResponseDto processingStart(UserRequestDto userRequestDto);
   StartConfigureResponseDto processingStateStartConfigure(Long tgId);
   RestartResponseDto restartOnboarding(Long tgId);
}
