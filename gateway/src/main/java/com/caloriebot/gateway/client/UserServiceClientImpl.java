package com.caloriebot.gateway.client;

import com.caloriebot.gateway.client.dto.UserRequestDto;
import com.caloriebot.gateway.client.dto.UserResponseDto;
import lombok.RequiredArgsConstructor;
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
}
