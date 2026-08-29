package com.caloriebot.gateway.client;

import com.caloriebot.gateway.client.dto.RestartResponseDto;
import com.caloriebot.gateway.client.dto.StartConfigureResponseDto;
import com.caloriebot.gateway.client.dto.UserRequestDto;
import com.caloriebot.gateway.client.dto.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class UserServiceClientImpl implements UserServiceClient {
    private final RestClient restClient;

    public UserResponseDto processingStart(UserRequestDto userRequestDto) {
        return restClient.post()
                .uri("/users/processing-start")
                .body(userRequestDto)
                .retrieve()
                .body(UserResponseDto.class);
    }

    public StartConfigureResponseDto processingStateStartConfigure(Long tgId) {
        return restClient.post()
                .uri("/users/by-telegram/{tgId}/onboarding/start-configure", tgId)
                .retrieve()
                // 409 - isn't error
                .onStatus(status -> status == HttpStatus.CONFLICT, (_, _) -> { })
                .body(StartConfigureResponseDto.class);
    }

    public RestartResponseDto restartOnboarding(Long tgId) {
        return restClient.post()
                .uri("/users/by-telegram/{tgId}/onboarding/restart", tgId)
                .retrieve()
                .body(RestartResponseDto.class);
    }
}
