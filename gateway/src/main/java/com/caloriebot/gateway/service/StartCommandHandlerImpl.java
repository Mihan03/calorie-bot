package com.caloriebot.gateway.service;

import com.caloriebot.gateway.client.UserServiceClient;
import com.caloriebot.gateway.client.dto.UserRequestDto;
import com.caloriebot.gateway.client.dto.UserResponseDto;
import com.caloriebot.gateway.enums.UserState;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Slf4j
@Service
@Getter
@RequiredArgsConstructor
public class StartCommandHandlerImpl implements StartCommandHandler {
    private final UserServiceClient userServiceClient;
    private final MessageService messageService;

    public SendMessage processStartHandler(Long tgId, Long chatId) {
        UserRequestDto userRequestDto = new UserRequestDto(tgId);
        UserResponseDto user = userServiceClient.processingStart(userRequestDto);

        log.info("Был получен user={} ", user.toString());

        return messageService.getMessageByScreen(StateMap.getScreen(UserState.NEW), chatId);
    }

    public SendMessage processRestartHandler(Long tgId, Long chatId) {
        userServiceClient.restartOnboarding(tgId);

        return messageService.getMessageByScreen(StateMap.getScreen(UserState.WAITING_WEIGHT), chatId);
    }
}
