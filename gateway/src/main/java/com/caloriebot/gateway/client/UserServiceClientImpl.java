package com.caloriebot.gateway.client;

import com.caloriebot.gateway.client.dto.StartConfigureDtoResponse;
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

    public StartConfigureDtoResponse processingStateStartConfigure(Long tgId) {
        return restClient.post()
                .uri("/users/by-telegram/{tgId}/onboarding/start-configure", tgId)
                .retrieve()
                // 409 - isn't error
                .onStatus(status -> status == HttpStatus.CONFLICT, (_, _) -> { })
                .body(StartConfigureDtoResponse.class);
    }

    public void restartOnboarding(Long tgId) {
        restClient.post()
                .uri("/users/by-telegram/{tgId}/onboarding/restart", tgId)
                .retrieve()
                .toBodilessEntity();
    }
}
