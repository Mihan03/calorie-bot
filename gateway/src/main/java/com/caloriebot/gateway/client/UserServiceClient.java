package com.caloriebot.gateway.client;

import com.caloriebot.gateway.client.dto.StartConfigureDtoResponse;
import com.caloriebot.gateway.client.dto.UserRequestDto;
import com.caloriebot.gateway.client.dto.UserResponseDto;

public interface UserServiceClient {
   UserResponseDto processingStart(UserRequestDto userRequestDto);
   StartConfigureDtoResponse processingStateStartConfigure(Long tgId);
   void restartOnboarding(Long tgId);
}
