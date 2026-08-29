package com.caloriebot.gateway.handler;

import com.caloriebot.gateway.client.UserServiceClient;
import com.caloriebot.gateway.client.dto.RestartResponseDto;
import com.caloriebot.gateway.client.dto.StartConfigureResponseDto;
import com.caloriebot.gateway.screen.BotMessage;
import com.caloriebot.gateway.UserState;
import com.caloriebot.gateway.service.MessageService;
import com.caloriebot.gateway.screen.Screen;
import com.caloriebot.gateway.screen.StateMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Service
@Slf4j
@RequiredArgsConstructor
public class StartConfigureCallbackHandlerImpl implements StartConfigureCallbackHandler {
    private final UserServiceClient userServiceClient;
    private final MessageService messageService;

    public SendMessage processStartConfigureHandler(Long tgId, Long chatId) {
        StartConfigureResponseDto response = userServiceClient.processingStateStartConfigure(tgId);

        return getSendMessage(chatId, response.applied(), response.userState());
    }

    public SendMessage processRestartHandler(Long tgId, Long chatId) {
        RestartResponseDto response = userServiceClient.restartOnboarding(tgId);

        return getSendMessage(chatId, response.applied(), response.userState());
    }

    private SendMessage getSendMessage(Long chatId, boolean applied, String s) {
        if (!applied) {
            UserState currentState = UserState.valueOf(s);
            Screen screen = StateMap.getScreen(currentState);

            return messageService.getMessageByScreen(screen, chatId, BotMessage.CURRENT_STEP.getText());
        }

        return messageService.getMessageByScreen(StateMap.getScreen(UserState.WAITING_WEIGHT), chatId);
    }
}
