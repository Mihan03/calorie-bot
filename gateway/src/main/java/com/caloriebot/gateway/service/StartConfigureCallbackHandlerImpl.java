package com.caloriebot.gateway.service;

import com.caloriebot.gateway.client.UserServiceClient;
import com.caloriebot.gateway.client.dto.StartConfigureDtoResponse;
import com.caloriebot.gateway.enums.BotMessage;
import com.caloriebot.gateway.enums.UserState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Service
@Slf4j
@RequiredArgsConstructor
public class StartConfigureCallbackHandlerImpl {
    private final UserServiceClient userServiceClient;
    private final MessageService messageService;

    public SendMessage processStartConfigureHandler(Long tgId, Long chatId) {
        StartConfigureDtoResponse response = userServiceClient.processingStateStartConfigure(tgId);

        if (!response.applied()) {
            UserState currentState = UserState.valueOf(response.userState());
            Screen screen = StateMap.getScreen(currentState);

            return messageService.getMessage(BotMessage.CURRENT_STEP.getText() + "\"" + screen.getMessage().getShortName() + "\". " +
                    screen.getMessage().getText(), chatId, null);
        }

        return messageService.getMessage(BotMessage.WAITING_WEIGHT.getText(), chatId, null);
    }
}
