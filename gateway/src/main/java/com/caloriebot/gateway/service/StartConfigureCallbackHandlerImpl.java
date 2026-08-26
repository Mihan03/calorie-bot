package com.caloriebot.gateway.service;

import com.caloriebot.gateway.client.UserServiceClient;
import com.caloriebot.gateway.client.dto.StartConfigureDtoResponse;
import com.caloriebot.gateway.enums.BotKeyboard;
import com.caloriebot.gateway.enums.BotMessage;
import com.caloriebot.gateway.enums.UserState;
import com.caloriebot.gateway.util.InlineKeyboardBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

@Service
@Slf4j
@RequiredArgsConstructor
public class StartConfigureCallbackHandlerImpl implements StartConfigureCallbackHandler {
    private final UserServiceClient userServiceClient;
    private final MessageService messageService;

    public SendMessage processStartConfigureHandler(Long tgId, Long chatId) {
        StartConfigureDtoResponse response = userServiceClient.processingStateStartConfigure(tgId);

        if (!response.applied()) {
            UserState currentState = UserState.valueOf(response.userState());

            Screen screen = StateMap.getScreen(currentState);

            InlineKeyboardMarkup markup = new InlineKeyboardBuilder().button(BotKeyboard.ONB_RESTART).build();
            return messageService.getMessage(BotMessage.CURRENT_STEP.getText() + "\"" + screen.getMessage().getShortName() + "\". " +
                    screen.getMessage().getText(), chatId, markup);
        }

        return messageService.getMessageByScreen(StateMap.getScreen(UserState.WAITING_WEIGHT), chatId);
    }
}
